inherit epics-module

SUMMARY = "Busy recipe"
DESCRIPTION = "Recipe for building busy for the EPICS control system."

# FIXME: Uncomment the LIC_FILES_CHKSUM/LICENSE_PATH and change LICENSE the next time Busy is updated
# latest release (R1-7-4) does not contain a LICENSE file, it was only added after the last tag
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://LICENSE;md5=a2c259c010f2152379d7769be894bf4a"
#LICENSE_PATH += "${S}"

SRCREV = "569a6b6fb1288c067ac2b22a998aa7de5375ddc4"
SRC_URI = "git://github.com/epics-modules/busy;protocol=https;branch=master;rev=${SRCREV}"

EPICS_DEPENDS += "epics-asyn"
DEPENDS += "${EPICS_DEPENDS}"

S = "${WORKDIR}/git"

python do_configure() {
    # Generate a RELEASE.local handling all dependencies.
    # Disable autosave, as it's only needed for the test apps
    epics.generate_release_local(d, {
        'AUTOSAVE': '',
        'BUSY': ''
    })

    # Retarget build products too
    epics.generate_config_site(d)
}
