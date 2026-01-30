inherit epics-module

SUMMARY = "SLAC ioc-common-gpio recipe"
DESCRIPTION = "Recipe for building a SLAC templated GPIO IOC for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=TODO"
LICENSE_PATH += "${S}"

SRCREV = "TODO"
SRC_URI = "git://github.com/pcdshub/ioc-common-gpio;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
