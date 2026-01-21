inherit epics-module

SUMMARY = "timingApi recipe"
DESCRIPTION = "Recipe for building SLAC's timingApi for the EPICS control system."

# FIXME: Change this once timingApi has a proper license assigned
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
#LICENSE_PATH += "${S}"
#NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRCREV = "458bf179e7ec80f13109cd94c4b0d762c2a1015f"
SRC_URI = "git://github.com/slac-epics/timingApi;branch=master;protocol=https;rev=${SRCREV}"

S = "${WORKDIR}/git"

do_compile() {
    echo "This recipe has nothing to do!"
}