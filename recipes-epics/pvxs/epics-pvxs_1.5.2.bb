# Recipe for pvxs

inherit epics-module

SUMMARY = "pvxs recipe"
DESCRIPTION = "Recipe for building EPICS pvxs for the EPICS control system."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3775480a712fc46a69647678acb234cb"
LICENSE_PATH += "${S}"

SRCREV = "8e00eaecdee5ce8a474704e70d820e6f92693fa1"
SRC_URI = "gitsm://github.com/epics-base/pvxs;protocol=https;branch=master;rev=${SRCREV}"

DEPENDS += "libevent"

copy_conflicting_files () {
    # HORRIBLE HACK INCOMING!
    # PVXS ordinarily generates CONFIG_SITE.Common.blah automatically. This conflicts with the files we just generated, so we need to provide them in a different format.
    mv -v "${S}/configure/CONFIG_SITE.Common.linux-${TARGET_ARCH}" "${S}/configure/CONFIG_SITE.linux-${BUILD_ARCH}.linux-${TARGET_ARCH}" 
    mv -v "${S}/configure/CONFIG_SITE.Common.linux-${BUILD_ARCH}" "${S}/configure/CONFIG_SITE.linux-${BUILD_ARCH}.linux-${BUILD_ARCH}"
    
    # Need to set PVXS to the install loc so EPICS can actually find our cfg/ subdir
    echo "PVXS=${D}/opt/epics/${MODNAME}" >> "${S}/configure/RELEASE.local"
}

do_configure[postfuncs] += "copy_conflicting_files"

install_tools() {
    install -d "${D}/usr/local/bin"

    # Symlink pvxs tools to /usr/local so they're on our PATH
    for tool in pvxcall pvxget pvxput pvxlist pvxmonitor pvxinfo pvxmshim pvxvct softIocPVX ;
    do
        ln -s "/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}/${tool}" "${D}/usr/local/bin/${tool}" 
    done
}

do_install[postfuncs] += "install_tools"

FILES:${PN} += "/usr/local/bin"

S = "${WORKDIR}/git"
