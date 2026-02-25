#
# Defines common methods and rules for EPICS module recipes
#

# Default module name is the package name
MODNAME ?= "${PN}"

# Add your EPICS dependencies to this variable
EPICS_DEPENDS = ""

# Enable static libs
EPICS_ENABLE_STATIC_LIBS ?= "1"

# Disable shared libs (by default)
EPICS_ENABLE_SHARED_LIBS ?= "0"

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

# We can only package files in a single package; this exposes perl tools that
# are necessary for both host and target builds, which are packaged with the
# target, to be available to the host build as well.
do_compile:prepend() {
    export PERL5LIB="${RECIPE_SYSROOT}/opt/epics/epics-base/lib/perl"
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

# See comment above; need to do this before tasks with compilation
do_install:prepend() {
    export PERL5LIB="${RECIPE_SYSROOT}/opt/epics/epics-base/lib/perl"
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
ALL_FILES += "/opt/epics/${MODNAME}/edl"


# Build a package for the build host
PACKAGES += "${PN}-native"

# Build a package for the target system
FILES:${PN}:append:class-target = " ${ALL_FILES}"
FILES:${PN}:append:class-target = " /opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}"
FILES:${PN}:append:class-target = " /opt/epics/${MODNAME}/lib/linux-${TARGET_ARCH}"

FILES:${PN}:append:class-native = " /opt/epics/${MODNAME}/bin/linux-${BUILD_ARCH}"
FILES:${PN}:append:class-native = " /opt/epics/${MODNAME}/lib/linux-${BUILD_ARCH}"

# Expose this package in the sysroot
SYSROOT_DIRS:append:class-target = " /opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}"
SYSROOT_DIRS:append:class-target = " /opt/epics/${MODNAME}/lib/linux-${TARGET_ARCH}"
SYSROOT_DIRS:append:class-target = " ${ALL_FILES}"

SYSROOT_DIRS_NATIVE:append = " ${STAGING_DIR_NATIVE}/opt/epics/${MODNAME}/bin/linux-${BUILD_ARCH}"
SYSROOT_DIRS_NATIVE:append = " ${STAGING_DIR_NATIVE}/opt/epics/${MODNAME}/lib/linux-${BUILD_ARCH}"
