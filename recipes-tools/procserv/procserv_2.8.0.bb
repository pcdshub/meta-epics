SUMMARY = "procServ recipe"
DESCRIPTION = "Builds procServ"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRCREV = "6ccbdc7e27d316dd5abd00ac89ea853ad7059d60"
SRC_URI = "git://github.com/ralphlange/procServ;protocol=https;branch=master;rev=${SRCREV}"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-doc"

inherit autotools