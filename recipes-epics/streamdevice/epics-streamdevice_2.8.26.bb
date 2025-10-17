inherit epics-module

SUMMARY = "StreamDevice recipe"
DESCRIPTION = "Recipe for building StreamDevice for the EPICS control system."

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"
LICENSE_PATH += "${S}"

SRCREV = "668d1d525509604ab4ccd7382022dfb469c99841"
SRC_URI = "git://github.com/paulscherrerinstitute/StreamDevice;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"

EPICS_DEPENDS += "epics-asyn epics-calc epics-sscan"
DEPENDS += "${EPICS_DEPENDS} libpcre"

set_pcre () {
    # Unset PCRE "location" for host builds; this causes us issues if the build machine doesn't have libpcre1
    echo "PCRE=" >> "${S}/configure/CONFIG_SITE.Common.linux-${BUILD_ARCH}"

    # Point it at the right libraries for cross builds
    echo "PCRE_INCLUDE=${RECIPE_SYSROOT}/usr/include" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"
    echo "PCRE_LIB=${RECIPE_SYSROOT}/usr/lib" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"
    echo "PCRE=" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"

    # Enable TIRPC, we need it on this glibc version!
    echo "TIRPC=YES" >> "${S}/configure/CONFIG_SITE.local"
}

do_configure[postfuncs] += "set_pcre"
