#
# Helper class containing functions that may be re-used in EPICS module/IOC
# recipes. 
#

set_pcre () {
    # Unset PCRE "location" for host builds; this causes us issues if the build machine doesn't have libpcre1
    echo "PCRE=" >> "${S}/configure/CONFIG_SITE.Common.linux-${BUILD_ARCH}"

    # Point it at the right libraries for cross builds
    echo "PCRE_INCLUDE=${RECIPE_SYSROOT}/usr/include" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"
    echo "PCRE_LIB=${RECIPE_SYSROOT}/usr/lib" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"
    echo "PCRE=" >> "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}"
}

set_tirpc () {
    # Enable TIRPC, we need it on this glibc version!
    echo "TIRPC=YES" >> "${S}/configure/CONFIG_SITE.local"
}

unset_busy () {
    echo "BUSY=" >> "${S}/configure/RELEASE.local"
}

unset_seq () {
    echo "SNCSEQ=" >> "${S}/configure/RELEASE.local"
}

# For motor record
# I don't think this will be needed for embedded targets
unset_ipac () {
    echo "IPAC=" >> "${S}/configure/RELEASE.local"
}

