inherit epics-module

SUMMARY = "drvAsynI2C recipe"
DESCRIPTION = "Recipe for building drvAsynI2C for the EPICS control system."

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f27defe1e96c2e1ecd4e0c9be8967949"
LICENSE_PATH += "${S}"

SRCREV = "605dfef5ca2739a2cc0257658ee7261210beab5d"
SRC_URI = "git://github.com/ffeldbauer/drvAsynI2C;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-asyn" 
DEPENDS += "${EPICS_DEPENDS} i2c-tools"

S = "${WORKDIR}/git"
