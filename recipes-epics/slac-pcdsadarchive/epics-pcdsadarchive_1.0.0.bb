inherit epics-module

SUMMARY = "PCDS AreaDetector Archive recipe"
DESCRIPTION = "Recipe for building PCDS's AreaDetector archive template files"

LICENSE = "pcdsarchive"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=124b474de52726467bac3d2591af9370"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[pcdsarchive] = "LICENSE.md"

# Only needs base, include by default in EPICS_DEPENDS
EPICS_DEPENDS += ""
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "369da668e7c95b05d99e077aa5a38eff69ea4e77"
SRC_URI = "git://github.com/slac-epics/pcds_ad_archive;branch=master;protocol=https;rev=${SRCREV}"

S = "${WORKDIR}/git"
