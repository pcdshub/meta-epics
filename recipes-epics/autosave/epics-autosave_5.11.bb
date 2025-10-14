inherit epics-module

SUMMARY = "Autosave recipe"
DESCRIPTION = "Recipe for building EPICS base for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[synApps] = "LICENSE"

SRCREV = "606903e177790c6431b277d4393700d9e5991b26"
SRC_URI = "git://github.com/epics-modules/autosave;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"
