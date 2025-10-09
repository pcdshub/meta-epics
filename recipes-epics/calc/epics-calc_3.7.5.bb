inherit epics-module

SUMMARY = "Calc recipe"
DESCRIPTION = "Recipe for building Calc for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/epics-modules/calc;protocol=https;branch=master;rev=R3-7-5"

DEPENDS += "epics-base"
DEPENDS += "epics-sscan"

S = "${WORKDIR}/git"
