inherit epics-module

SUMMARY = "SLAC ADSupport recipe"
DESCRIPTION = "Recipe for building SLAC's fork of ADSupport for the EPICS control system."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f58d4a2e30a77fa9529619ccce87dd8b"
LICENSE_PATH += "${S}"

SRCREV = "5b7ea230796ab48c52e455b98339a1e787d54b0a"
SRC_URI = "git://github.com/slac-epics/ADSupport;protocol=https;branch=R1.9-0.branch;rev=${SRCREV}"

S = "${WORKDIR}/git"

config_libs () {
    # Bug fix: Ensure WITH_XXX XXX_EXTERNAL vars are set so we build local packages.
    echo 'include $(TOP)/configure/CONFIG_ADSupport' >> "${S}/configure/CONFIG_SITE.local"
}

do_configure[postfuncs] += "config_libs"
