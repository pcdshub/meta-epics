#
# class for all EPICS modules that automatically brings in epics-base and epics-base-native
#

inherit epics-component epics-functions

# All EPICS modules depend on epics-base and epics-base-native
DEPENDS += "epics-base epics-base-native"

# We should always have epics-base on target
EPICS_DEPENDS += "epics-base"

# Add all EPICS dependencies to the image
RDEPENDS:${PN} += "${EPICS_DEPENDS}"

PACKAGE_PREPROCESS_FUNCS += "sanitize_module"

sanitize_module () {
    if [ -d "${PKGD}/opt/epics/${MODNAME}" ]; then
        # Install and sanitize all configuration files
        # Do RECIPE_SYSROOT_NATIVE first to avoid conflict with RECIPE_SYSROOT
        cp -rfv "${S}/configure" "${PKGD}/opt/epics/${MODNAME}"
        find "${PKGD}/opt/epics/${MODNAME}/configure" -type f -name 'CONFIG*' -exec sed -i "s,${RECIPE_SYSROOT}/image,,g" {} \;
        find "${PKGD}/opt/epics/${MODNAME}/configure" -type f -name 'CONFIG*' -exec sed -i "s,${RECIPE_SYSROOT_NATIVE},,g" {} \;
        find "${PKGD}/opt/epics/${MODNAME}/configure" -type f -name 'CONFIG*' -exec sed -i "s,${RECIPE_SYSROOT},,g" {} \;
        find "${PKGD}/opt/epics/${MODNAME}/configure" -type f -name 'CONFIG*' -exec sed -i "s,${D},,g" {} \;
        # Sanitize all installed .local files
        find "${PKGD}/opt/epics/${MODNAME}" -type f -iname '*.local' -exec sed -i "s,${RECIPE_SYSROOT},,g" {} \;

        # Sanitize envPaths
        find "${PKGD}/opt/epics/${MODNAME}" -type f -name 'envPaths' -exec sed -i "s,${RECIPE_SYSROOT},,g" {} \;
    fi

    # Fix EPICS_BASE_HOST_BIN/LIB on target
    if [ -f "${PKGD}/opt/epics/${MODNAME}/configure/CONFIG_SITE.local" ]; then
        sed -i -e "s/${BUILD_ARCH}/${TARGET_ARCH}/g" "${PKGD}/opt/epics/${MODNAME}/configure/CONFIG_SITE.local"
    fi
}
