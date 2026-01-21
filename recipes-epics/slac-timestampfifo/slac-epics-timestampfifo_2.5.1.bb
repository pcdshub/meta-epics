inherit epics-module

SUMMARY = "timeStampFifo recipe"
DESCRIPTION = "Recipe for building SLAC's timeStampFifo for the EPICS control system."

# FIXME: Change this once timeStampFifo has a proper license assigned
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

EPICS_DEPENDS += "slac-epics-adcore slac-epics-diagtimer slac-epics-timingapi epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "16dd344f73df730918541807b63b3917974639ec"
SRC_URI = "git://github.com/slac-epics/timeStampFifo;branch=slac-trunk;protocol=https;rev=${SRCREV}"

S = "${WORKDIR}/git"

config_tweaks() {
    # Common mapping DIAGTIMER -> DIAG_TIMER, TIMINGAPI -> TIMING_API
    echo 'DIAG_TIMER=$(DIAGTIMER)\n' >> "${S}/configure/RELEASE.local"
    echo 'TIMING_API=$(TIMINGAPI)\n' >> "${S}/configure/RELEASE.local"
}

do_configure[postfuncs] += "config_tweaks"
