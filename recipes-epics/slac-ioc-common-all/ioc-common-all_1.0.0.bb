SUMMARY = "SLAC LCLS ECS IOC common environment"
DESCRIPTION = "Repository containing common scripts and environment variable definitions for EPICS IOCS."

# No license file for this repo
LICENSE = "CLOSED"

# This repo doesn't have any tags, and I'm not sure if it should. For now we just call the
# latest commit "1.0.0". 
SRC_URI = "git://git@github.com/pcdshub/iocCommon-All;protocol=ssh;branch=pcds-All;rev=19a2902bade6f8bc35883e3fec0993ff59b3441b"

S = "${WORKDIR}/git"

do_install () {
    INSTALLDIR="${D}/opt/epics/${PN}"
    install -d "$INSTALLDIR"

    install -m 0755 ${S}/pre_linux.cmd "$INSTALLDIR"
    install -m 0755 ${S}/post_linux.cmd "$INSTALLDIR"
}

FILES:${PN} += " \
    /opt/epics/${PN}/pre_linux.cmd \
    /opt/epics/${PN}/post_linux.cmd \
"
