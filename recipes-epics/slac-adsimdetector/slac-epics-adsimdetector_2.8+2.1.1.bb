inherit epics-module

SUMMARY = "ADSimDetector recipe"
DESCRIPTION = "Recipe for building SLAC's ADSimDetector for the EPICS control system."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f58d4a2e30a77fa9529619ccce87dd8b"
LICENSE_PATH += "${S}"

SRCREV = "2fa25455aff64b0e5825633aad2a41a3a7489dc1"
SRC_URI = "git://github.com/slac-epics/ADSimDetector;branch=R2.8-2.branch;protocol=https;rev=${SRCREV}"

EPICS_DEPENDS += "slac-epics-adcore epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
