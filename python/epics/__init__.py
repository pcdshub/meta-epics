#!/usr/bin/env python3
"""
Common utilities for configuring/building EPICS packages in Yocto
"""
import os
import sys
import subprocess
import io

def _sanitize_compiler_cmd(cc: str):
    return cc.split(' ')[0]

def _cat_file(path: str):
    with open(path, 'r') as fp:
        print(fp.read())

def _strip_compiler(d, var: str) -> str:
    """
    Process a $CC/$CXX/$LD arg.
    Strip the compiler off the front of it and return the rest of the string
    """
    s: str = d.getVar(var)
    for k in s:
        if k == ' ': break
        s = s[1:]
    return s

def host_arch(d) -> str:
    """
    Determines host arch based on uname()
    """
    host_os = d.getVar('BUILD_OS')
    return f'{host_os.split("-")[0]}-{d.getVar("BUILD_ARCH")}'

def target_arch(d) -> str:
    """
    Returns EPICS-ified target arch (i.e. linux-aarch64)
    """
    tgt_arch = d.getVar('TARGET_ARCH')
    return f'linux-{tgt_arch}'

def get_extra_compiler_flags(d) -> list[str]:
    """
    Returns additional compiler flags for GCC. Must be passed to
    the EPICS build system in one way or another
    """
    return [
        f'--sysroot={d.getVar("RECIPE_SYSROOT")}'
    ]

def get_depends(d) -> dict:
    """
    Returns a dict of EPICS modules that this one depends on
    This is a mapping of recipe name -> path to the package.
    """
    r = {}
    # Some EPICS dependencies have names that do not conform to the standard;
    # for example the sequencer module is often listed as SNCSEQ in RELEASE. 
    # Here we keep a dictionary of non-standard dependencies and their corrected
    # RELEASE variable.
    alt_release = {
        'epics-sequencer': 'epics-sncseq',
        'epics-streamdevice-i2c': 'epics-streamdevice'
    }

    pfx = d.getVar('RECIPE_SYSROOT')
    deps = d.getVar('EPICS_DEPENDS')
    for dep in deps.split(' '):
        if len(dep) == 0: continue
        if dep in alt_release.keys(): dep = alt_release[dep]
        r[dep] = f'{pfx}/opt/epics/{dep}'
    return r

def generate_release_local(d, extra: dict = {}):
    """
    Generates a configure/RELEASE.local to get a module ready for build
    Reads the DEPENDS variable to determine which EPICS packages we depend on
    
    Parameters
    ----------
    d : Any
        Build context
    extra : dict
        Extra variables to add to the RELEASE.local.
        This is a NAME -> VALUE mapping
    """
    root = d.getVar('RECIPE_SYSROOT')
    with open('configure/RELEASE.local', 'w') as fp:
        fp.seek(0, io.SEEK_END) # Ensure we append, in case important content exists
        fp.write(f'EPICS_BASE={root}/opt/epics/epics-base\n')
        # Write out modules and their associated paths
        deps = get_depends(d)
        for mn, mv in deps.items():
            # convert to a usable variable in RELEASE
            # FIXME: May cause issues with modules like seq, which is sometimes referred to as SNCSEQ in the RELEASE
            mn = mn.replace('slac-epics-', '').replace('epics-', '').upper()
            fp.write(f'{mn}={mv}\n')
        fp.write('SUPPORT=\n')
        # write out extras
        for e, v in extra.items():
            fp.write(f'{e}={v}\n')
    print('Generated configure/RELEASE.local:')
    _cat_file('configure/RELEASE.local')

def generate_config_site(d, extra: dict = {}):
    """
    Generates a configure/CONFIG_SITE.local to get a module ready for build
    Configures the install location to point at /opt/ somewhere
    
    Parameters
    ----------
    d : Any
        Build context
    extra : dict
        Extra variables to add to the end of CONFIG_SITE.local
        Name -> value mapping
    """
    pfx = d.getVar('D')
    mn = d.getVar('MODNAME')
    root = d.getVar('RECIPE_SYSROOT')
    native_root = d.getVar('RECIPE_SYSROOT_NATIVE')
    harch = host_arch(d)
    # SLAC modules do not support CONFIG_SITE.local, we must use CONFIG_SITE.$(HOST_ARCH).Common instead
    for fn in ['CONFIG_SITE.local', f'CONFIG_SITE.{host_arch(d)}.Common']:
        with open(f'configure/{fn}', 'w') as fp:
            fp.seek(0, io.SEEK_END)
            fp.write(f'EPICS_BASE_HOST_BIN={native_root}/opt/epics/epics-base/bin/{harch}\n')
            # Tweak location of build products
            fp.write(f'INSTALL_LOCATION={pfx}/opt/epics/{mn}\n')
            fp.write(f'FINAL_LOCATION=/opt/epics/{mn}\n')
            # iocBoot/cpuBoot will be installed here too, but only run under the target.
            fp.write(f'IOCS_APPL_TOP=/opt/epics/{mn}\n')
            # Disable CHECK_RELEASE. Simply not compatile with Yocto due to the different sysroots used to compile
            # each package. Our EPICS_BASE location is never the same between packages.
            fp.write('CHECK_RELEASE=NO\n')
            # Enable/disable static and shared libs
            fp.write(f'STATIC_BUILD={"YES" if d.getVar("EPICS_ENABLE_STATIC_LIBS") == "1" else "NO"}\n')
            fp.write(f'SHARED_LIBRARIES={"YES" if d.getVar("EPICS_ENABLE_SHARED_LIBS") == "1" else "NO"}\n')
            # For shared objects, we want to use $ORIGIN for the RPATH, so we don't end up with invalid temporary paths in the shared objects.
            fp.write('LINKER_USE_RPATH=ORIGIN\n')
            # Enable host build when requested
            if d.getVar('ENABLE_HOST_PACKAGE') == '1':
                fp.write('HOST_BUILD=YES\n')
            # append extras
            for e, v in extra.items():
                fp.write(f'{e}={v}\n')
        print(f'Generated configure/{fn}:')
        _cat_file(f'configure/{fn}')

    # Generate a CONFIG_SITE specifying target options
    target_cfg_site = f'configure/CONFIG_SITE.Common.{target_arch(d)}'
    with open(target_cfg_site, 'w') as fp:
        fp.seek(0, io.SEEK_END)
        # The compiler variables $CC/$CXX/$LD in Yocto have a bunch of flags appended. These are not added to CC/CXX/LD in EPICS base,
        # so we need to add them here.
        fp.write(f'USR_CXXFLAGS+={_strip_compiler(d, "CXX")} {d.getVar("CXXFLAGS")}\n')
        fp.write(f'USR_CPPFLAGS+={_strip_compiler(d, "CC")} {d.getVar("CPPFLAGS")}\n')
        fp.write(f'USR_CFLAGS+={_strip_compiler(d, "CC")} {d.getVar("CFLAGS")}\n')
        fp.write(f'USR_LDFLAGS+={_strip_compiler(d, "LD")} {d.getVar("LDFLAGS")}\n')

    # Generate a CONFIG_SITE for HOST options
    host_cfg_site = f'configure/CONFIG_SITE.Common.{host_arch(d)}'
    with open(host_cfg_site, 'w') as fp:
        fp.seek(0, io.SEEK_END)
        fp.write(f'USR_CXXFLAGS+={d.getVar("BUILD_CXXFLAGS")}\n')
        fp.write(f'USR_CPPFLAGS+={d.getVar("BUILD_CPPFLAGS")}\n')
        fp.write(f'USR_CFLAGS+={d.getVar("BUILD_CFLAGS")}\n')
        fp.write(f'USR_LDFLAGS+={d.getVar("BUILD_LDFLAGS")}\n')

    print(f'Generated {target_cfg_site}:')
    _cat_file(target_cfg_site)

