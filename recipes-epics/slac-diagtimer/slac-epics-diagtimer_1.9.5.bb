inherit epics-module

SUMMARY = "diagTimer recipe"
DESCRIPTION = "Recipe for building SLAC's diagTimer for the EPICS control system."

# FIXME: Change this once diagTimer has a proper license assigned
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRCREV = "99d1c9412e608513d25db558383b78d8c427ecf0"
SRC_URI = "git://github.com/slac-epics/diagTimer;branch=slac-master;protocol=https;rev=${SRCREV}"

S = "${WORKDIR}/git"
