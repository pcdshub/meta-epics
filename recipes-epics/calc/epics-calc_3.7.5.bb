inherit epics-module

SUMMARY = "Calc recipe"
DESCRIPTION = "Recipe for building Calc for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[synApps] = "LICENSE"

SRCREV = "2f5b175f260bc3fe35bc25a3f6c204e9d6f628c9"
SRC_URI = "git://github.com/epics-modules/calc;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-sscan"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
