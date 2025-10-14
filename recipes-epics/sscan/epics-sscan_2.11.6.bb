inherit epics-module

SUMMARY = "Calc recipe"
DESCRIPTION = "Recipe for building Calc for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"
NO_GENERIC_LICENSE[synApps] = "LICENSE"

SRCREV = "f03da19eb11a5aa648d63cf1f26d95acd6172e28"
SRC_URI = "git://github.com/epics-modules/sscan;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"

disable_sncseq () {
    # Disable sequencer
    echo "SNCSEQ=" >> "${S}/configure/RELEASE.local"
}

do_configure[postfuncs] += "disable_sncseq"
