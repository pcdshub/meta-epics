inherit epics-module

SUMMARY = "miscUtils recipe"
DESCRIPTION = "Recipe for building SLAC's miscUtils for the EPICS control system."

# FIXME: Change this once miscUtils has a proper license assigned
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRC_URI = "git://github.com/slac-epics/miscUtils;branch=slac-master;protocol=https"
SRCREV = "24a4ddfb59cd7053476452d1178104a4116b2c30"

S = "${WORKDIR}/git"
