inherit epics-module epics-functions

SUMMARY = "EPICS ADAravis recipe"
DESCRIPTION = "Recipe for building the ADAravis areaDetector module for the EPICS control system."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"
LICENSE_PATH += "${S}"

SRCREV = "2f79ca52be181dff4f47ba9c17dc30710b488759"
SRC_URI = "git://github.com/areaDetector/ADAravis;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-areadetector epics-asyn"
DEPENDS += "${EPICS_DEPENDS} aravis"

S = "${WORKDIR}/git"

configure_stuff() {
    echo "ARAVIS_INCLUDE=${RECIPE_SYSROOT}/usr/include/aravis-0.8" >> "${S}/configure/CONFIG_SITE.local"
    echo "GLIB_INCLUDE=${RECIPE_SYSROOT}/usr/include/glib-2.0 ${RECIPE_SYSROOT}/usr/lib/glib-2.0/include" >> "${S}/configure/CONFIG_SITE.local"
}

do_configure[postfuncs] += "set_areadetector"
do_configure[postfuncs] += "configure_stuff"
