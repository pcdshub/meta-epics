inherit epics-module

SUMMARY = "SLAC ffmpegServer recipe"
DESCRIPTION = "Recipe for building SLAC's fork of ffmpegServer for the EPICS control system."

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
LICENSE_PATH += "${S}"

EPICS_DEPENDS += "slac-epics-nullhttpd slac-epics-adcore epics-asyn"
DEPENDS += "xz bzip2 ffmpeg zlib ${EPICS_DEPENDS}"


SRCREV = "797b779ca1662b0154bd623889d31d187f066a07"
SRC_URI = "git://github.com/slac-epics/ffmpegServer;protocol=https;branch=slac-main;rev=${SRCREV}"

S = "${WORKDIR}/git"
