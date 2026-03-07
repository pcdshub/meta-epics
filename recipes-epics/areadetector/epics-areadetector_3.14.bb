# Recipe for areaDetector master branch

inherit epics-module

SUMMARY = "areaDetector recipe"
DESCRIPTION = "Recipe for building the areaDetector superpackage for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f58d4a2e30a77fa9529619ccce87dd8b"
LICENSE_PATH += "${S}"

SRCREV = "ed51ecf4f5d781f8edb525cd4219902c82b0be94"
SRC_URI = "gitsm://github.com/areaDetector/areaDetector;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-autosave epics-calc epics-asyn epics-pvxs epics-sscan"
DEPENDS += "${EPICS_DEPENDS} python3-native zlib tiff ffmpeg"

configure_libs() {
    # Note on RELEASE_PRODS/RELEASE_LIBS:
    # - Must use absolute paths because $(TOP) is different between the two
    # - RELEASE_PRODS is included by test IOCs in the downstream modules. TOP=../..
    # - RELEASE_LIBS is included by downstream modules themselves. TOP=..

    echo "AREA_DETECTOR=${S}" >> "${S}/configure/RELEASE.local"

    # Point at ADSupport/ADCore
    echo 'ADSUPPORT=$(AREA_DETECTOR)/ADSupport' >> "${S}/configure/RELEASE.local"
    echo 'ADCORE=$(AREA_DETECTOR)/ADCore' >> "${S}/configure/RELEASE.local"

    # Create RELEASE_PRODS and RELEASE_LIBS for submoduled packages
    cp "${S}/configure/RELEASE.local" "${S}/configure/RELEASE_PRODS.local"
    cp "${S}/configure/RELEASE.local" "${S}/configure/RELEASE_LIBS.local"

    # Enable commonly used/utility modules
    for pkg in ADSimDetector ADCSimDetector ffmpegServer NDDriverStdArrays pvaDriver \
        ADGenICam
    do
        echo "$(echo ${pkg} | tr '[:lower:]' '[:upper:]')=\$(AREA_DETECTOR)/${pkg}" >> "${S}/configure/RELEASE.local"
    done

    # Use these Yocto-provided packages:
    for pkg in ZLIB TIFF; do
        echo "WITH_${pkg} = YES" >> "${S}/configure/CONFIG_SITE.local"
        echo "${pkg}_EXTERNAL = YES" >> "${S}/configure/CONFIG_SITE.local"
    done

    # The rest need to be built by ADSupport
    # - JPEG : Yocto only has OpenJPEG which I believe is not libjpeg
    # - HDF5 : The one provided by Yocto is too new
    # - XML2 : Yocto provided version also too new
    for pkg in JPEG NETCDF NEXUS SZIP BLOSC HDF5 XML2; do
        echo "WITH_${pkg} = YES" >> "${S}/configure/CONFIG_SITE.local"
        echo "${pkg}_EXTERNAL = NO" >> "${S}/configure/CONFIG_SITE.local"
    done

    echo "WITH_PVXS = YES" >> "${S}/configure/CONFIG_SITE.local"
    echo "WITH_PVA = YES" >> "${S}/configure/CONFIG_SITE.local"
    
    # HACK: ffmpegServer disables all cross arches, so we can't rely on the inherited value from EPICS base
    echo "CROSS_COMPILER_TARGET_ARCHS=linux-${TARGET_ARCH}" >> "${S}/configure/CONFIG_SITE.local"

    # Aravis specific config
    echo "ARAVIS_INCLUDE = ${RECIPE_SYSROOT}/usr/include" >> "${S}/configure/CONFIG_SITE.local"
    echo "GLIB_INCLUDE = ${RECIPE_SYSROOT}/usr/include/glib-2.0 ${RECIPE_SYSROOT}/usr/lib/glib-2.0/include" >> "${S}/configure/CONFIG_SITE.local"
    echo "glib-2.0_DIR =  ${RECIPE_SYSROOT}/usr/lib" >> "${S}/configure/CONFIG_SITE.local"
}

do_configure[postfuncs] += "configure_libs"

S = "${WORKDIR}/git"
