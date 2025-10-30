inherit epics-component

SUMMARY = "EPICS base recipe"
DESCRIPTION = "Recipe for building EPICS base, the core component of the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

BBCLASSEXTEND =+ "native nativesdk"

SRCREV = "07572ab02593fa225660fdee670850c9989f5851"
SRC_URI = "gitsm://github.com/epics-base/epics-base;protocol=https;branch=7.0;rev=${SRCREV}"

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

    # NOTE: Build is actually done during the do_install() process
    # Installing files here will result in them being clobbered before do_install.
    # make build.${TGTARCH} would work (in theory), but the build process relies on msi
    # and other EPICS tools that are generated+installed as part of the install.xxx targets.
}

do_compile() {
    echo "Skipping; all work done in do_install"
}

do_install() {
    install_dir="${D}/opt/epics/${MODNAME}"

    # Build base with the build host flags
    make -j${BB_NUMBER_THREADS} \
        USR_CFLAGS="${BUILD_CFLAGS}" \
        USR_CXXFLAGS="${BUILD_CXXFLAGS}" \
        USR_LDFLAGS="${BUILD_LDFLAGS}" \
        install.linux-${BUILD_ARCH}

    # Bring in the env flags. These must be supplied on the command line *only* because they
    # may contain package specific settings (i.e. --sysroot=). Putting them in a CONFIG_SITE.Common file
    # will result in them being passed down to other EPICS packages.
    make -j${BB_NUMBER_THREADS} \
        USR_CFLAGS="${CFLAGS}" \
        USR_CXXFLAGS="${CXXFLAGS}" \
        USR_LDFLAGS="${LDFLAGS}" \
        install.linux-${TARGET_ARCH}

    # Need to remove these so we pass the stupid tmpdir sanity check...
    rm -rf "${install_dir}/lib/pkgconfig"

    mkdir -p "${D}/usr/local/bin"
    for prog in caput caget cainfo camonitor catime caRepeater pvcall pvget pvinfo pvlist pvmonitor pvput
    do
        ln -s /opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}/$prog "${D}/usr/local/bin/$prog"
    done

    # The generated caRepeater.service uses paths in our Yocto tmpdir. Re-run the substitution ourselves and install
    mkdir -p "${D}/etc/systemd/system/multi-user.target.wants"
    cp "modules/ca/src/client/caRepeater.service@" "${D}/etc/systemd/system/caRepeater.service"
    sed -i "s,@INSTALL_BIN@,/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH},g" "${D}/etc/systemd/system/caRepeater.service"
    chmod 644 "${D}/etc/systemd/system/caRepeater.service"
    ln -s "/etc/systemd/system/caRepeater.service" "${D}/etc/systemd/system/multi-user.target.wants/caRepeater.service"
}

FILES:${PN} += "/usr/local/bin/*"
FILES:${PN} += "/etc/systemd/system"