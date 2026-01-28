inherit epics-module

SUMMARY = "devGpioGeneric recipe"
DESCRIPTION = "Recipe for building devGpioGeneric for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=827a9a5c5c77a5911ff27bb91a5c51ee"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/JJL772/devGpioGeneric;protocol=https;branch=main;rev=${SRCREV}"

S = "${WORKDIR}/git"
