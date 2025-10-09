inherit epics-module

SUMMARY = "Autosave recipe"
DESCRIPTION = "Recipe for building EPICS base for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/epics-modules/autosave;protocol=https;branch=master;rev=R5-11"

DEPENDS += "epics-base"

S = "${WORKDIR}/git"
