#
# Defines common methods and rules for EPICS module recipes
#

# Default module name is the package name
MODNAME ?= "${PN}"

# Add your EPICS dependencies to this variable
EPICS_DEPENDS = ""

# Disable other checks that are incompatible with the EPICS build style
# - staticdev is incompatible with packages that include static libs in the base package.
# - file-rdeps is incompatible with combined sdk/target packages, where we have a mix of host tools and target bins.
# - arch is incompatible with -native packages, since they contain binaries for the build host OS
# - buildpaths is to suppress complaints about the build host (class-native)
#   package containing references to TMPDIR, which is fine because these are
#   not installed on the target, only used in compilation of the target package
INSANE_SKIP:class-target = "file-rdeps staticdev"
INSANE_SKIP:class-native = "file-rdeps staticdev arch buildpaths"

# Sucks, but we have to compile the package for x86_64 hosts too in case some tools run during the build process
COMPATIBLE_HOST = "(x86_64|aarch64|arm).*linux.*"

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

    # Copy iocBoot and cpuBoot directories
    for d in iocBoot cpuBoot; do
        if [ -d $d ]; then
            cp -rfv $d "${D}/opt/epics/${MODNAME}/$d"
        fi
    done
}

# Common directories to install for both native and target pkgs
ALL_FILES += "/opt/epics/${MODNAME}/db"
ALL_FILES += "/opt/epics/${MODNAME}/dbd"
ALL_FILES += "/opt/epics/${MODNAME}/include"
ALL_FILES += "/opt/epics/${MODNAME}/configure"
ALL_FILES += "/opt/epics/${MODNAME}/cfg"
ALL_FILES += "/opt/epics/${MODNAME}/templates"
ALL_FILES += "/opt/epics/${MODNAME}/doc"
ALL_FILES += "/opt/epics/${MODNAME}/html"
ALL_FILES += "/opt/epics/${MODNAME}/iocBoot"
ALL_FILES += "/opt/epics/${MODNAME}/cpuBoot"
ALL_FILES += "/opt/epics/${MODNAME}/autosave"
ALL_FILES += "/opt/epics/${MODNAME}/display"
ALL_FILES += "/opt/epics/${MODNAME}/screens"
ALL_FILES += "/opt/epics/${MODNAME}/archive"
ALL_FILES += "/opt/epics/${MODNAME}/iocsh"
ALL_FILES += "/opt/epics/${MODNAME}/children"
ALL_FILES += "/opt/epics/${MODNAME}/src/tools"
ALL_FILES += "/opt/epics/${MODNAME}/lib/perl"


# Build a package for the build host
PACKAGES += "${PN}-native"
FILES:${PN}-native += "/opt/epics/${MODNAME}/bin/linux-${BUILD_ARCH}"
FILES:${PN}-native += "/opt/epics/${MODNAME}/lib/linux-${BUILD_ARCH}"

# Build a package for the target system
FILES:${PN} += "${ALL_FILES}"
FILES:${PN} += "/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}"
FILES:${PN} += "/opt/epics/${MODNAME}/lib/linux-${TARGET_ARCH}"

# Expose this package in the sysroot
SYSROOT_DIRS += "${ALL_FILES}"
SYSROOT_DIRS += "/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}"
SYSROOT_DIRS += "/opt/epics/${MODNAME}/lib/linux-${TARGET_ARCH}"

SYSROOT_DIRS_NATIVE += "/opt/epics/${MODNAME}/bin/linux-${BUILD_ARCH}"
SYSROOT_DIRS_NATIVE += "/opt/epics/${MODNAME}/lib/linux-${BUILD_ARCH}"
