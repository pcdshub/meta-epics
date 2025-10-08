#
# Defines common methods for EPICS module recipes
#

# Default module name is the package name
MODNAME = "${PN}"

# Disable other checks that are incompatible with the EPICS build style
# - staticdev is incompatible with packages that include static libs in the base package.
# - file-rdeps is incompatible with combined sdk/target packages, where we have a mix of host tools and target bins.
# - arch is incompatible with combined native/nativesdk packages
INSANE_SKIP:${PN} = "file-rdeps staticdev arch"

# Disable stripping and debug split; doesn't work for combined packages like this
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Sucks, but we have to compile the package for x86_64 hosts too in case some tools run during the build process
COMPATIBLE_HOST = "(x86_64|aarch64).*-linux"

python do_configure() {
    print(f'host arch={epics.host_arch(d)}')
    print(f'epics target={epics.target_arch(d)}')

    print(f'RECIPE_SYSROOT={d.getVar("RECIPE_SYSROOT")}')

    # Generate a RELEASE.local handling all dependencies
    epics.generate_release_local(d)

    # Retarget build products too
    epics.generate_config_site(d)
}

do_compile() {
    make -j${BB_NUMBER_THREADS} build
}

do_install() {
    make -j${BB_NUMBER_THREADS} install
}

FILES:${PN} += "/opt/epics/${MODNAME}/*"

# Pack together a -dev package so we can expose these files to other recipes
SYSROOT_DIRS += "/opt/epics/${MODNAME}"
FILES_${PN}-dev += "/opt/epics/${MODNAME}/*"