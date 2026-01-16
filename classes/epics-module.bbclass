#
# class for all EPICS modules that automatically brings in epics-base and epics-base-native
#

inherit epics-component

# All EPICS modules depend on epics-base and epics-base-native
DEPENDS += "epics-base epics-base-native"

# Add all EPICS dependencies to the image
RDEPENDS:${PN} += "${EPICS_DEPENDS}"

do_install:append() {
    # Sanitize module RELEASE.local
    sed -i "s,${RECIPE_SYSROOT},,g" "${D}/opt/epics/${MODNAME}/configure/RELEASE.local"

    # Sanitize envPaths
    find "${D}/opt/epics/${MODNAME}" -type f -name 'envPaths' -exec sed -i "s,${RECIPE_SYSROOT},,g" {} \;
}
