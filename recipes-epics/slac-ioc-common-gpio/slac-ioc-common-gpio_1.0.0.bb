inherit slac-templated-ioc

SUMMARY = "SLAC ioc-common-gpio recipe"
DESCRIPTION = "Recipe for building a SLAC templated GPIO IOC for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1e0e2c6c30de74e30aa6af57b7c31003"
LICENSE_PATH += "${S}"

# Remove slac-epics-iocadmin for now
EPICS_DEPENDS += "epics-autosave"

SRCREV = "41b3bc2f3be8279aa48ae201c3f201f2a905d665"
SRC_URI = "git://git@github.com/slactjohnson/ioc-common-gpio.git;protocol=ssh;branch=working;rev=${SRCREV}"

S = "${WORKDIR}/git"
