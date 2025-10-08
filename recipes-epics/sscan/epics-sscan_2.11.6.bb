inherit epics-module

SUMMARY = "Calc recipe"
DESCRIPTION = "Recipe for building Calc for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/epics-modules/sscan;protocol=https;branch=master;rev=R2-11-6"

DEPENDS += "epics-base"

S = "${WORKDIR}/git"

disable_sncseq () {
    # Disable sequencer
    echo "SNCSEQ=" >> "${S}/configure/RELEASE.local"
}

do_configure[postfuncs] += "disable_sncseq"
