inherit epics-module

SUMMARY = "asynDribble recipe"
DESCRIPTION = "Recipe for building asynDribble for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d5d2d0aba00b060efa3b9de96c8fdd0"
LICENSE_PATH += "${S}"

EPICS_DEPENDS += "epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "1004ae41af2356e17981ac9607b96ad9c38da69e"
SRC_URI = "git://github.com/slac-epics/asynDribble;protocol=https;branch=main;rev=${SRCREV}"

S = "${WORKDIR}/git"
