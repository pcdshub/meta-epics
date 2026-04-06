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

set_areadetector() {
    # Alias AREA_DETECTOR (used by many AD modules), and other modules packaged with areaDetector to AREADETECTOR (what we actually generate)
    for m in AREA_DETECTOR ADCORE ADSUPPORT ADSIMDETECTOR ADCSIMDETECTOR FFMPEGSERVER ADGENICAM
    do
        echo "$m=\$(AREADETECTOR)" >> "${S}/configure/RELEASE.local"
    done
}

# For motor record
# I don't think this will be needed for embedded targets
unset_ipac () {
    echo "IPAC=" >> "${S}/configure/RELEASE.local"
}

# Update envPaths to include definitions specified by IOC_ENV
python update_env_paths() {
    D = d.getVar("D")
    MODNAME = d.getVar("MODNAME")
    IOC_PATH = d.getVar("IOC_PATH")

    import os
    with open(f"{D}/opt/epics/{MODNAME}/{IOC_PATH}/envPaths", "a") as fp:
        for k in d.getVar("IOC_ENV").split(" "):
            if len(k) == 0: continue
            V = d.getVar(f"{k}_ENV")
            if V is None: V = ""
            fp.write(f"epicsEnvSet(\"{k}\", \"{V}\")\n")
}
