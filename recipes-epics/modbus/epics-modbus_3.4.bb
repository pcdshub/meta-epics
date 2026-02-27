inherit epics-module

SUMMARY = "EPICS modbus recipe"
DESCRIPTION = "Recipe for building the modbus module for the EPICS control system."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=43057147b5f02dd83806671b26596a9d"
LICENSE_PATH += "${S}"

SRCREV = "4a1276b0efa6f378a6dcbe58a10a1d73a44c7ed3"
SRC_URI = "git://github.com/epics-modules/modbus;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
