inherit epics-module

SUMMARY = "iocAdmin recipe"
DESCRIPTION = "Recipe for building SLAC's iocAdmin for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f8d58a6d07f6899acb6c0e0a594f55da"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRCREV = "a53df77b905dd991e58251cf82f4390a6a84a42e"
SRC_URI = "git://github.com/slac-epics/iocAdmin;protocol=https;branch=R3.1.16-1.branch;rev=${SRCREV}"

S = "${WORKDIR}/git"
