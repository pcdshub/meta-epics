inherit epics-module

SUMMARY = "ADAravis recipe"
DESCRIPTION = "Recipe for building SLAC's ADAravis for the EPICS control system."

# FIXME: Change this when ADAravis has a license assigned
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=f58d4a2e30a77fa9529619ccce87dd8b"
#LICENSE_PATH += "${S}"

SRCREV = "958801ed8c2ce569cb0505f7fa0a981d6b198e46"
SRC_URI = "git://github.com/slac-epics/ADAravis;branch=master;protocol=https;rev=${SRCREV}"

EPICS_DEPENDS += "slac-epics-adcore epics-asyn slac-epics-adgenicam"
DEPENDS += "${EPICS_DEPENDS} libusb glib-2.0 aravis util-linux libffi libpcre glibmm libpam"

S = "${WORKDIR}/git"

config_libs () {
    # Force a CONFIG_SITE.local include...
    echo 'include $(TOP)/configure/CONFIG_SITE.local' >> "${S}/configure/CONFIG_SITE"

    # These must be unset, otherwise it tries to install all libraries in $(GLIBPREFIX)
    echo "GLIBPREFIX="      >> "${S}/configure/CONFIG_SITE.local"

    # Static only, and use system libraries/includes
    echo "USE_SYSTEM_LIBS=YES"      >> "${S}/configure/CONFIG_SITE.local"
    echo "SHARED_LIBRARIES=NO"      >> "${S}/configure/CONFIG_SITE.local"

    # Redirect package paths
    echo "ARAVISPREFIX=${RECIPE_SYSROOT}/usr"    >> "${S}/configure/CONFIG_SITE.local"
    echo "FFI_TOP=${RECIPE_SYSROOT}/usr"         >> "${S}/configure/CONFIG_SITE.local"
    echo "PCRE_TOP=${RECIPE_SYSROOT}/usr"        >> "${S}/configure/CONFIG_SITE.local"
    echo "LIBUSB_TOP=${RECIPE_SYSROOT}/usr"      >> "${S}/configure/CONFIG_SITE.local"
    echo "GLIBMM_TOP=${RECIPE_SYSROOT}/usr"      >> "${S}/configure/CONFIG_SITE.local"
    echo "UTIL_LINUX_TOP=${RECIPE_SYSROOT}/usr"  >> "${S}/configure/CONFIG_SITE.local"
    echo "FFI_LIB=${RECIPE_SYSROOT}/usr/lib"     >> "${S}/configure/CONFIG_SITE.local"
    echo "PCRE_LIB=${RECIPE_SYSROOT}/usr/lib"    >> "${S}/configure/CONFIG_SITE.local"
    
    # Additional includes to appease compiler. I feel like there's a better way to do this.
    echo "USR_CPPFLAGS += -I\"${RECIPE_SYSROOT}/usr/include/glib-2.0\""     >> "${S}/configure/CONFIG_SITE.local"
    echo "USR_CPPFLAGS += -I\"${RECIPE_SYSROOT}/usr/lib/glib-2.0/include\"" >> "${S}/configure/CONFIG_SITE.local"
}

do_configure[postfuncs] += "config_libs"
