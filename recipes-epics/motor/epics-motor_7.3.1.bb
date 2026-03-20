# Recipe for EPICS motor module

inherit epics-module

SUMMARY = "EPICS motor  recipe"
DESCRIPTION = "Recipe for building EPICS motor module for the EPICS control system."

# No license file in repo
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=76d18f9132055ed510b481f6f211e0d7"
#LICENSE_PATH += "${S}"

SRCREV = "88c627ae02a2c26bbec391d15fd6fa3239e47477"
SRC_URI = "gitsm://github.com/epics-modules/motor;protocol=https;branch=master;rev=${SRCREV} \
           file://0001-Add-top-level-arch-configs-to-module-builds.patch \
           "

S = "${WORKDIR}/git"

MODNAME = "epics-motor"

# Skipping busy, sequencer, IPAC, Lua, MX, and modbus for now
EPICS_DEPENDS += "epics-asyn"

DEPENDS += "${EPICS_DEPENDS}"

do_configure[postfuncs] += "unset_busy"
do_configure[postfuncs] += "unset_seq"
do_configure[postfuncs] += "unset_ipac"
