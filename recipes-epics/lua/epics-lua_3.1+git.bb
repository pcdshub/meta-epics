inherit epics-module

SUMMARY = "EPICS Lua recipe"
DESCRIPTION = "Recipe for building the Lua module for the EPICS control system."

# TODO: Update me when EPICS LUA gets a license
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=3000208d539ec061b899bce1d9ce9404"
#LICENSE_PATH += "${S}"

SRCREV = "17475b50ae95c801bad9573bb2b031ffb04ac75c"
SRC_URI = "git://github.com/epics-modules/lua;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-asyn" 
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"
