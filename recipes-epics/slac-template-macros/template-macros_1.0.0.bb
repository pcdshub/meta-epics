SUMMARY = "SLAC LCLS ECS IOC template macro expansion tools"
DESCRIPTION = "Repository containing tools used in the expansion of 'templated' EPICS IOCS into final products."

LICENSE_PATH += "${WORKDIR}/git"
LICENSE = "LICENSE.md"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2ed04a81f93145a8e913e64266452100"

SRCREV = "3bba479c7f6aa550da0e2e5538abd3a4755161ec"
SRC_URI = "git://git@github.com/pcdshub/ioc-template-macros.git;protocol=ssh;branch=master;rev=${SRCREV} \
           file://0001-Add-rules-for-systemd-service-and-startup-script.patch \
           "

S = "${WORKDIR}/git"

RDEPENDS:${PN} = "bash python3"

do_compile () {
    # The make file is really simple, and tries to use the build host GCC. 
    # This replicates the repository makefile, but uses the toolchain setup 
    # for the target system.
    ${CC} -o realpath realpath.c ${LDFLAGS}
}

do_install () {
    INSTALLDIR="${D}/opt/epics/${PN}"
    install -d "$INSTALLDIR"

    install -m 0755 ${S}/RULES_EXPAND "$INSTALLDIR"
    install -m 0755 ${S}/expand "$INSTALLDIR"
    install -m 0755 ${S}/expand.py "$INSTALLDIR"
    install -m 0755 ${S}/realpath "$INSTALLDIR"
}

FILES:${PN} += " \
    /opt/epics/${PN}/RULES_EXPAND \
    /opt/epics/${PN}/expand \
    /opt/epics/${PN}/expand.py \
    /opt/epics/${PN}/realpath \
"

SYSROOT_DIRS += "/opt/epics/${PN}"
