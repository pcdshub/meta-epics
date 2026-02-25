# Recipe for caPutLog

inherit epics-module

SUMMARY = "caPutLog recipe"
DESCRIPTION = "Recipe for building EPICS caPutLog for the EPICS control system."

# caPutLog has no license yet
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=76d18f9132055ed510b481f6f211e0d7"
#LICENSE_PATH += "${S}"

SRCREV = "bfbea4ac6f358a63d7ddacdcfc204202f508c10a"
SRC_URI = "git://github.com/epics-modules/caPutLog;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"