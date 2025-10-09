#!/usr/bin/env python3
"""
Common utilities for configuring/building EPICS packages in Yocto
"""
import os
import sys
import subprocess

def _sanitize_compiler_cmd(cc: str):
    return cc.split(' ')[0]

def _cat_file(path: str):
    with open(path, 'r') as fp:
        print(fp.read())

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
        f'--sysroot={d.getVar('RECIPE_SYSROOT')}'
    ]

def is_epics_package(d, name: str) -> str|None:
    """
    Determine if a package in DEPENDS is an EPICS package (base, module, etc)
    Returns the full path to it, or None if it isn't an EPICS package
    """
    pfx = d.getVar('RECIPE_SYSROOT')
    path = f'{pfx}/opt/epics/{name}'
    # Should pass for everything EPICS related
    if os.path.exists(f'{path}/configure/RELEASE'):
        return path
    return None

def get_depends(d) -> dict:
    """
    Returns a dict of EPICS modules that this one depends on
    This is a mapping of recipe name -> path to the package.
    """
    pfx = d.getVar('RECIPE_SYSROOT')
    deps = d.getVar('DEPENDS').split(' ')
    r = {}
    for dep in deps:
        if dep == 'epics-base':
            continue # This is implied
        l = is_epics_package(d, dep)
        if l:
            r[dep] = l
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
        fp.write(f'EPICS_BASE={root}/opt/epics/epics-base\n')
        # Write out modules and their associated paths
        deps = get_depends(d)
        for mn, mv in deps.items():
            # convert to a usable variable in RELEASE
            mn = mn.replace('epics-', '').upper()
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
    pn = d.getVar('PN')
    with open('configure/CONFIG_SITE.local', 'w') as fp:
        # Tweak location of build products
        fp.write(f'INSTALL_LOCATION={pfx}/opt/epics/{pn}\n')
        fp.write(f'FINAL_LOCATION={pfx}/opt/epics/{pn}\n')
        # Disable CHECK_RELEASE. Simply not compatile with Yocto due to the different sysroots used to compile
        # each package. Our EPICS_BASE location is never the same between packages.
        fp.write('CHECK_RELEASE=NO\n')
        # append extras
        for e, v in extra.items():
            fp.write(f'{e}={v}\n')
    print('Generated configure/CONFIG_SITE.local:')
    _cat_file('configure/CONFIG_SITE.local')

    # Generate a CONFIG_SITE specifying target options
    target_cfg_site = f'configure/CONFIG_SITE.Common.{target_arch(d)}'
    with open(target_cfg_site, 'w') as fp:
        # append additional compiler/linker flags. Bit of a hack, but we only know these flags
        # NOW, and not when we configured EPICS base. This is all a product of Yocto's sandboxing...
        sysroot_arg = f'--sysroot={d.getVar("RECIPE_SYSROOT")}'
        fp.write(f'USR_CXXFLAGS+={sysroot_arg} {d.getVar("CXXFLAGS")}\n')
        fp.write(f'USR_CPPFLAGS+={sysroot_arg} {d.getVar("CPPFLAGS")}\n')
        fp.write(f'USR_CFLAGS+={sysroot_arg} {d.getVar("CFLAGS")}\n')
        fp.write(f'USR_LDFLAGS+={sysroot_arg} {d.getVar("LDFLAGS")}\n')

    # Generate a CONFIG_SITE for HOST options
    host_cfg_site = f'configure/CONFIG_SITE.Common.{host_arch(d)}'
    with open(host_cfg_site, 'w') as fp:
        fp.write(f'USR_CXXFLAGS+={d.getVar("BUILD_CXXFLAGS")}\n')
        fp.write(f'USR_CPPFLAGS+={d.getVar("BUILD_CPPFLAGS")}\n')
        fp.write(f'USR_CFLAGS+={d.getVar("BUILD_CFLAGS")}\n')
        fp.write(f'USR_LDFLAGS+={d.getVar("BUILD_LDFLAGS")}\n')

    print(f'Generated {target_cfg_site}:')
    _cat_file(target_cfg_site)

