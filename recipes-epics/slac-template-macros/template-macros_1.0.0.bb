SUMMARY = "SLAC LCLS ECS IOC template macro expansion tools"
DESCRIPTION = "Repository containing tools used in the expansion of 'templated' EPICS IOCS into final products."

LICENSE_PATH += "${WORKDIR}/git"
LICENSE = "LICENSE.md"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2ed04a81f93145a8e913e64266452100"

SRC_URI = "git://git@github.com/pcdshub/ioc-template-macros.git;protocol=ssh;branch=master"

PV = "1.0.0"
SRCREV = "R1.0.0"

S = "${WORKDIR}/git"

RDEPENDS:${PN} = "bash python3"

do_compile () {
    # The make file is really simple, and tries to use the build host GCC. 
    # This replicates the repository makefile, but uses the toolchain setup 
    # for the target system.
    ${CC} -o realpath realpath.c ${LDFLAGS}
}

do_install () {
    INSTALLDIR="${D}/opt/epics/${PN}/${PV}"
    install -d "$INSTALLDIR"

    install -m 0755 ${S}/RULES_EXPAND "$INSTALLDIR"
    install -m 0755 ${S}/expand "$INSTALLDIR"
    install -m 0755 ${S}/expand.py "$INSTALLDIR"
    install -m 0755 ${S}/realpath "$INSTALLDIR"
}

FILES:${PN} += " \
    /opt/epics/${PN}/${PV}/RULES_EXPAND \
    /opt/epics/${PN}/${PV}/expand \
    /opt/epics/${PN}/${PV}/expand.py \
    /opt/epics/${PN}/${PV}/realpath \
"
