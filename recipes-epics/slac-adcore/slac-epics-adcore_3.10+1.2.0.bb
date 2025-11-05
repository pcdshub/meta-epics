inherit epics-module

SUMMARY = "SLAC ADCore recipe"
DESCRIPTION = "Recipe for building SLAC's fork of ADCore for the EPICS control system."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f58d4a2e30a77fa9529619ccce87dd8b"
LICENSE_PATH += "${S}"

EPICS_DEPENDS += "slac-epics-adsupport epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

SRCREV = "0b856f786a46b46440b4cf5a2657d8a29346bbb2"
SRC_URI = "git://github.com/slac-epics/ADCore;protocol=https;branch=R3.10-1.branch;rev=${SRCREV}"

S = "${WORKDIR}/git"

config_tweaks () {
    # HACK!!: Let's disable SHARED_LIBRARIES for host builds, otherwise the native package provides the same
    # shared libraries as the target package. Yocto will tell us to take a hike if we do this...
    echo "SHARED_LIBRARIES=NO" >> "${S}/configure/CONFIG_SITE.Common.linux-${BUILD_ARCH}"
}

do_configure[postfuncs] += "config_tweaks"
