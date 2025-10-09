inherit epics-module

SUMMARY = "Asyn recipe"
DESCRIPTION = "Recipe for building asyn for the EPICS control system."

LICENSE = "synApps"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9f42f43716fb1d5e8498617125cb3c21"
LICENSE_PATH += "${S}"

SRC_URI = "git://github.com/epics-modules/asyn;protocol=https;branch=master;rev=R4-45"

DEPENDS += "epics-base libtirpc"

S = "${WORKDIR}/git"

python do_configure() {
    # Generate a RELEASE.local handling all dependencies
    epics.generate_release_local(d)

    # Retarget build products too, enable TIRPC
    epics.generate_config_site(d, {
        "TIRPC": "YES"
    })
}
