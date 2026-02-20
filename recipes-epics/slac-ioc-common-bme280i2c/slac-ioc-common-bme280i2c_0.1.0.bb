inherit slac-templated-ioc

SUMMARY = "SLAC ioc-common-bme280i2c recipe"
DESCRIPTION = "Recipe for building a SLAC templated BME280 IOC for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1e0e2c6c30de74e30aa6af57b7c31003"
LICENSE_PATH += "${S}"

# Remove slac-epics-iocadmin for now
EPICS_DEPENDS += "epics-autosave epics-streamdevice epics-drvasyni2c"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "bce3871eff6b45c8be9e62d492b77b513922d57f"
SRC_URI = "git://git@github.com/slactjohnson/ioc-common-bme280I2C.git;protocol=ssh;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
