inherit epics-module

SUMMARY = "ADStream recipe"
DESCRIPTION = "Recipe for building SLAC's ADStream for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=319f5e68af4c69a3c9655bae2e386b14"
LICENSE_PATH += "${S}"

SRCREV = "05e3252405bcc253e06cc91e304a9b0ccb916430"
SRC_URI = "git://github.com/slac-epics/ADStream;branch=slac-master;protocol=https;rev=${SRCREV}"

EPICS_DEPENDS += "slac-epics-adcore"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
