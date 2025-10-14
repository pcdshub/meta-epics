inherit epics-module

SUMMARY = "MCoreUtils recipe"
DESCRIPTION = "Recipe for building MCoreUtils for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[EPICS] = "LICENSE"

SRCREV = "8990b59f180552fbf33697be04190381f3075643"
SRC_URI = "git://github.com/epics-modules/MCoreUtils;protocol=https;branch=main;rev=${SRCREV}"

S = "${WORKDIR}/git"
