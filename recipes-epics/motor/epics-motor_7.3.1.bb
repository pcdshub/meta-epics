# Recipe for EPICS motor module

inherit epics-module

SUMMARY = "EPICS motor  recipe"
DESCRIPTION = "Recipe for building EPICS motor module for the EPICS control system."

# No license file in repo
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=76d18f9132055ed510b481f6f211e0d7"
#LICENSE_PATH += "${S}"

SRCREV = "88c627ae02a2c26bbec391d15fd6fa3239e47477"
SRC_URI = "gitsm://github.com/epics-modules/motor;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"

MODNAME = "epics-motor"

# Skipping busy, sequencer, IPAC, Lua, MX, and modbus for now
EPICS_DEPENDS += "epics-asyn"

DEPENDS += "${EPICS_DEPENDS}"

# I don't think this will be needed for embedded targets
unset_ipac () {
    echo "IPAC=" >> "${S}/configure/RELEASE.local"
}

# Need to tell submodules where our tools are. May be a better way to do this,
# but I couldn't figure it out.
set_module_host_bin () {
    echo "EPICS_BASE_HOST_BIN = ${RECIPE_SYSROOT_NATIVE}/opt/epics/epics-base/bin/${BUILD_OS}-${BUILD_ARCH}" >> "${S}/modules/CONFIG_SITE.local"
}

# We can't check release with yocto builds
unset_module_check_release () {
    echo "CHECK_RELEASE = NO" >> "${S}/modules/CONFIG_SITE.local"
}

do_configure[postfuncs] += "unset_busy"
do_configure[postfuncs] += "unset_seq"
do_configure[postfuncs] += "unset_ipac"
do_configure[postfuncs] += "set_module_host_bin"
do_configure[postfuncs] += "unset_module_check_release"
