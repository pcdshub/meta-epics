inherit epics-module

SUMMARY = "EPICS scaler recipe"
DESCRIPTION = "Recipe for building the scaler module for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"

SRCREV = "eddb4394381d98342e7c55b51bdcc6af30c3443c"
SRC_URI = "git://github.com/epics-modules/scaler;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
