inherit epics-component

SUMMARY = "EPICS base recipe"
DESCRIPTION = "Recipe for building EPICS base, the core component of the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"

BBCLASSEXTEND = "native nativesdk"

# Force MODNAME to epics-base for both native and target recipe
MODNAME = "epics-base"

SRCREV = "07572ab02593fa225660fdee670850c9989f5851"
SRC_URI = "gitsm://github.com/epics-base/epics-base;protocol=https;branch=7.0;rev=${SRCREV}"

SRC_URI += " file://0001-host-build-option.patch "

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
        # Enable/disable static and shared libs
        fp.write(f'STATIC_BUILD={"YES" if d.getVar("EPICS_ENABLE_STATIC_LIBS") == "1" else "NO"}\n')
        fp.write(f'SHARED_LIBRARIES={"YES" if d.getVar("EPICS_ENABLE_SHARED_LIBS") == "1" else "NO"}\n')
        fp.write(f'CROSS_COMPILER_TARGET_ARCHS={target_arch}\n')

        # Point at /opt/epics; better to do this here to avoid bad file paths
        fp.write(f'INSTALL_LOCATION={install_dir}\n')
        fp.write(f'FINAL_LOCATION=/opt/epics/{PN}\n')

        # Build only for target architecture(s), not for the build host
        fp.write('HOST_BUILD=NO\n')

    host_arch = epics.host_arch(d)

    # Grab compile tools
    CC = d.getVar("CC")
    CXX = d.getVar("CXX")
    LD = d.getVar("LD")
    AR = d.getVar("AR")
    RANLIB = d.getVar("RANLIB")

    # Append some barebones stuff to the host configuration
    with open(f'configure/os/CONFIG_SITE.Common.{host_arch}', 'a') as fp:
        fp.seek(0, io.SEEK_END)
        # Force C99; GCC 15 switches the default C standard to C23, which breaks a *lot* of things.
        fp.write('OP_SYS_CFLAGS += -std=c99\n')

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
}

do_compile() {
    # Bring in the env flags. These must be supplied on the command line *only* because they
    # may contain package specific settings (i.e. --sysroot=). Putting them in a CONFIG_SITE.Common file
    # will result in them being passed down to other EPICS packages.
    # Build base with the build host and target flags
    make -j${BB_NUMBER_THREADS} \
        USR_CFLAGS="${BUILD_CFLAGS}" \
        USR_CXXFLAGS="${BUILD_CXXFLAGS}" \
        USR_LDFLAGS="${BUILD_LDFLAGS}" \
        install.linux-${BUILD_ARCH}
}

do_compile:append:class-target() {
    make -j${BB_NUMBER_THREADS} \
        USR_CFLAGS="${CFLAGS}" \
        USR_CXXFLAGS="${CXXFLAGS}" \
        USR_LDFLAGS="${LDFLAGS}" \
        install.linux-${TARGET_ARCH}
}

do_install() {
    install_dir="${D}/opt/epics/${MODNAME}"

    # Install built or otherwise useful EPICS files
    # Arch specific files are handled in do_install:append functions below
    for subdir in configure cfg db dbd include templates; do
        install -d ${install_dir}/$subdir
        cp -RP --preserve=mode,links -v ${S}/$subdir/* ${install_dir}/$subdir
    done

    # Install EPICS Perl tools
    install -d ${install_dir}/src/tools
    cp -RP --preserve=mode,links -v ${S}/src/tools/* ${install_dir}/src/tools

    # Install more EPICS Perl tools
    # Have to be more specific here rather than copying _everything_ because
    # of libCap5.so that gets generated for the build host under this directory
    for subdir in DBD EPICS Pod URI; do
        install -d ${install_dir}/lib/perl/${subdir}
        cp -RP --preserve=mode,links -v ${S}/lib/perl/${subdir}/* ${install_dir}/lib/perl/${subdir}
    done

    for fname in CA.pm DBD.pm EpicsHostArch.pl; do
        install -m 0755 ${S}/lib/perl/$fname ${install_dir}/lib/perl/$fname
    done

    # Regardless of target or native build, the TARGET_ARCH is correct
    install_bin="${D}/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}"
    install -d ${install_bin}
    cp -RP --preserve=mode,links -v ${S}/bin/linux-${TARGET_ARCH}/* ${install_bin}

    install_lib="${D}/opt/epics/${MODNAME}/lib/linux-${TARGET_ARCH}"
    install -d ${install_lib}
    cp -RP --preserve=mode,links -v ${S}/lib/linux-${TARGET_ARCH}/* ${install_lib}
}

do_install:append:class-native() {
    native_bin="${D}${STAGING_DIR_NATIVE}/opt/epics/${MODNAME}/bin/linux-${BUILD_ARCH}"
    install -d ${native_bin}
    cp -RP --preserve=mode,links -v ${S}/bin/linux-${BUILD_ARCH}/* ${native_bin}

    native_lib="${D}${STAGING_DIR_NATIVE}/opt/epics/${MODNAME}/lib/linux-${BUILD_ARCH}"
    install -d ${native_lib}
    cp -RP --preserve=mode,links -v ${S}/lib/linux-${BUILD_ARCH}/* ${native_lib}
}

do_install:append:class-target() {
    # Symlink commonly used EPICS CLI tools
    mkdir -p "${D}/usr/local/bin"
    for prog in caput caget cainfo camonitor catime caRepeater pvcall pvget pvinfo pvlist pvmonitor pvput
    do
        ln -s /opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}/$prog "${D}/usr/local/bin/$prog"
    done

    # Install the generated caRepeater.service
    mkdir -p "${D}/etc/systemd/system/multi-user.target.wants"
    cp "${D}/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}/caRepeater.service" "${D}/etc/systemd/system/caRepeater.service"
    chmod 644 "${D}/etc/systemd/system/caRepeater.service"
    ln -s "/etc/systemd/system/caRepeater.service" "${D}/etc/systemd/system/multi-user.target.wants/caRepeater.service"
}

FILES:${PN}:append:class-target = " /usr/local/bin"
FILES:${PN}:append:class-target = " /etc/systemd/system"
