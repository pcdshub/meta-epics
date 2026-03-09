inherit epics-module

SUMMARY = "History recipe"
DESCRIPTION = "Recipe for building SLAC's history for the EPICS control system."

# FIXME: Update when history has a license
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRCREV = "6cbf5170d5be489c24b21b7f9d2f9d3e44444728"
SRC_URI = "git://github.com/slac-epics/history;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
