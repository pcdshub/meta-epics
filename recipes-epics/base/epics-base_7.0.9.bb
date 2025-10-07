SUMMARY = "EPICS base recipe"
DESCRIPTION = "Recipe for building EPICS base for the EPICS control system."

LICENSE = "LICENSE"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"

BBCLASSEXTEND =+ "native nativesdk"
COMPATIBLE_HOST = "(x86_64|aarch64).*-linux"

SRC_URI = "gitsm://github.com/epics-base/epics-base;protocol=https;branch=7.0;rev=07572ab02593fa225660fdee670850c9989f5851"

DEPENDS += " readline"

RDEPENDS:${PN} += " bash perl"

S = "${WORKDIR}/git"

python do_configure() {
    import os, subprocess, io

    dest = d.getVar("D")
    PN = d.getVar("PN")

    target_arch = epics.target_arch(d)
    
    print(f'TARGET_ARCH={target_arch}')
    print(f'HOST_ARCH={epics.host_arch(d)}')

    install_dir = f"{dest}/opt/epics/{PN}"
    try:
        os.mkdir(install_dir)
    except:
        pass

    # Write out a CONFIG_SITE.local with our changes
    with open('configure/CONFIG_SITE.local', 'w') as fp:
        # Disable shared libraries entirely
        fp.write('SHARED_LIBRARIES=NO\n')
        # Build static libraries
        fp.write('STATIC_BUILD=YES\n')
        fp.write(f'CROSS_COMPILER_TARGET_ARCHS={target_arch}\n')
        # Point at /opt/epics; better to do this here to avoid bad file paths
        fp.write(f'INSTALL_LOCATION={install_dir}\n')
        fp.write(f'FINAL_LOCATION={install_dir}\n')

    host_arch = epics.host_arch(d)

    # Grab compile tools
    CC = d.getVar("CC")
    CXX = d.getVar("CXX")
    LD = d.getVar("LD")
    AR = d.getVar("AR")
    RANLIB = d.getVar("RANLIB")

    if not os.path.exists(f'configure/os/CONFIG_SITE.{host_arch}.{target_arch}'):
        raise Exception(f'Target architecture {target_arch} is unsupported by EPICS base. Cannot continue')

    # This file can be safely overwritten
    with open(f'configure/os/CONFIG_SITE.{host_arch}.{target_arch}', 'w') as fp:
        # Set compile tools
        fp.write(f'CC={CC}\n')
        fp.write(f'CXX={CXX}\n')
        fp.write(f'CCC={CXX}\n')
        fp.write(f'LD={LD} -r\n')      # -r is ordinarily appended by EPICS base, but not here because we overrode LD directly
        fp.write(f'AR={AR} -rc\n')     # ...same situation with -rc
        fp.write(f'RANLIB={RANLIB}\n')

        # Ensure we use GNU hash style, because that's what Yocto expects...
        # Can't pull in the entire BUILD_LDFLAGS var here, that needs to be done on the command line
        fp.write(f'USR_LDFLAGS+=-Wl,--hash-style=gnu\n')

    # NOTE: Build is actually done during the do_install() process
    # Installing files here will result in them being clobbered before do_install.
    # make build.${TGTARCH} would work (in theory), but the build process relies on msi
    # and other EPICS tools that are generated+installed as part of the install.xxx targets.
}

do_compile() {
    echo "Skipping; all work done in do_install"
}

python do_install() {
    import os, subprocess, shutil

    D = d.getVar('D')
    PN = d.getVar('PN')

    install_dir = f'{D}/opt/epics/{PN}'

    r = subprocess.run([
        'make',
        f'install.{epics.target_arch(d)}',
        f'-j{os.cpu_count()}',
        # Bring in the BUILD_XXX flags. These must be supplied on the command line *only* because they
        # may contain package specific settings (i.e. --sysroot=). Putting them in a CONFIG_SITE.Common file
        # will result in them being passed down to other EPICS packages.
        f'USR_CFLAGS={d.getVar("BUILD_CFLAGS")} {d.getVar("CFLAGS")}',
        f'USR_CXXFLAGS={d.getVar("BUILD_CXXFLAGS")} {d.getVar("CXXFLAGS")}',
        f'USR_LDFLAGS={d.getVar("BUILD_LDFLAGS")} {d.getVar("LDFLAGS")}'
    ])

    if r.returncode != 0:
        raise Exception('Build failed')

    r = subprocess.run([
        'make', 'clean'
    ])

    if r.returncode != 0:
        raise Exception('Clean failed')

    # Need to remove these so we pass the stupid tmpdir sanity check...
    try:
        shutil.rmtree(f'{install_dir}/lib/pkgconfig')
    except:
        pass
}

# Disable stripping and debug split; doesn't work for combined packages like this
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Disable other checks that are incompatible here
# 1. arch needs to be skipped because we install certain host tools too (i.e. msi)
# 2. file-rdeps needs to be skipped for the same reason
# 3. staticdev is needed because we include static libs in the base package
INSANE_SKIP:${PN} = "staticdev file-rdeps arch"

# Ensure we're staged to the sysroot for our deps
SYSROOT_DIRS += "/opt/epics"

FILES:${PN} += "/opt/epics/${PN}/*"
FILES_${PN}-dev += "/opt/epics/${PN}/*"
