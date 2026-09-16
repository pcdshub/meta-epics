inherit epics-module

SUMMARY = "devLinuxAdc recipe"
DESCRIPTION = "EPICS device support for ADCs using the Linux IIO subsystem."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5770194bd6893813aaf63e972c4e49ad"

SRCREV = "1b818705c78920a38439bbf53f40c1c4bb7bb303"
SRC_URI = "git://git@github.com/slac-epics/devLinuxAdc;branch=main;protocol=ssh;rev=${SRCREV}"

S = "${WORKDIR}/git"
