inherit slac-templated-ioc

SUMMARY = "SLAC ioc-common-gpio recipe"
DESCRIPTION = "Recipe for building a SLAC templated GPIO IOC for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1e0e2c6c30de74e30aa6af57b7c31003"
LICENSE_PATH += "${S}"

# Remove slac-epics-iocadmin for now
EPICS_DEPENDS += "epics-autosave epics-devgpiogeneric"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "a2e708f4cd90004e5c34439c9b47cb1e75058767"
SRC_URI = "git://git@github.com/pcdshub/ioc-common-gpio.git;protocol=ssh;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
