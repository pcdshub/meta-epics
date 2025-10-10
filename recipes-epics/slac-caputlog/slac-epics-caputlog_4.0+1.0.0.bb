inherit epics-module

SUMMARY = "caPutLog recipe"
DESCRIPTION = "Recipe for building SLAC's caPutLog for the EPICS control system."

# FIXME: Change this once caPutLog upstream has a proper license assigned
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRC_URI = "git://github.com/slac-epics/caPutLog;branch=R4.0-1.branch;protocol=https;rev=R4.0-1.0.0"

DEPENDS += "epics-base"

S = "${WORKDIR}/git"
