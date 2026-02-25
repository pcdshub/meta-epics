# Recipe for iocStats

inherit epics-module

SUMMARY = "iocSats recipe"
DESCRIPTION = "Recipe for building EPICS iocStats for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=76d18f9132055ed510b481f6f211e0d7"
LICENSE_PATH += "${S}"

SRCREV = "c7ae6119661264f351c0c3f37a4ab042fa32f25d"
SRC_URI = "git://github.com/epics-modules/iocStats;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
