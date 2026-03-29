inherit epics-module

SUMMARY = "EPICS linStat recipe"
DESCRIPTION = "Recipe for building the linStat module for the EPICS control system."

LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3000208d539ec061b899bce1d9ce9404"
LICENSE_PATH += "${S}"

SRCREV = "a470a5954ff7be81bb35e10fa09597e8bf8d6722"
SRC_URI = "git://github.com/mdavidsaver/linstat;protocol=https;branch=master;rev=${SRCREV}"

SRC_URI += " \
            file://0001-toolchain-respect-cppflags.patch \
           "

S = "${WORKDIR}/git"
