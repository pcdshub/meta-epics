inherit epics-module

SUMMARY = "EPICS linStat recipe"
DESCRIPTION = "Recipe for building the linStat module for the EPICS control system."

LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3000208d539ec061b899bce1d9ce9404"
LICENSE_PATH += "${S}"

SRCREV = "ba299ea487df67ce6aee0877ed8dcc719ed2cd8a"
SRC_URI = "git://github.com/mdavidsaver/linstat;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
