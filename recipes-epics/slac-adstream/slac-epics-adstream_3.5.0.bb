inherit epics-module epics-functions

SUMMARY = "SLAC EPICS ADStream recipe"
DESCRIPTION = "Recipe for building the ADStream areaDetector module from SLAC, for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=319f5e68af4c69a3c9655bae2e386b14"
LICENSE_PATH += "${S}"

SRCREV = "a439c993aa846fd67de49379f10d46124b77d634"
SRC_URI = "git://github.com/slac-epics/ADStream;protocol=https;branch=slac-master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-areadetector"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"

do_configure[postfuncs] += "set_areadetector"
