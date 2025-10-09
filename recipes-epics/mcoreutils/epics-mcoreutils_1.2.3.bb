inherit epics-module

SUMMARY = "MCoreUtils recipe"
DESCRIPTION = "Recipe for building MCoreUtils for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/epics-modules/MCoreUtils;protocol=https;branch=main;rev=1.2.3"

DEPENDS += "epics-base"

S = "${WORKDIR}/git"
