inherit epics-module

SUMMARY = "SLAC ADGenICam recipe"
DESCRIPTION = "Recipe for building SLAC's fork of ADGenICam for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=50d0157d9bec42219ceb621409c980e8"
LICENSE_PATH += "${S}"

EPICS_DEPENDS += "slac-epics-adcore epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "704c4404e975862c869e8b1bda60bb18b6860ef9"
SRC_URI = "git://github.com/slac-epics/ADGenICam;protocol=https;branch=R1.10-2.branch;rev=${SRCREV}"

S = "${WORKDIR}/git"
