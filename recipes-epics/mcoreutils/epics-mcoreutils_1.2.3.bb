inherit epics-module

SUMMARY = "MCoreUtils recipe"
DESCRIPTION = "Recipe for building MCoreUtils for the EPICS control system."

LICENSE = "EPICS"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eeea17a15fc6ba8501fdcec09b854dc"
LICENSE_PATH += "${S}"

SRCREV = "8990b59f180552fbf33697be04190381f3075643"
SRC_URI = "git://github.com/epics-modules/MCoreUtils;protocol=https;branch=main;rev=${SRCREV}"

S = "${WORKDIR}/git"

fixup_example_app() {
    # The example app is an embedded top under MCoreUtils. It handles CONFIG_SITE.local correctly (includes it from the real top),
    # but doesnt handle CONFIG_SITE.ARCH correctly. So we must duplicate them
    cp -iv "${S}/configure/CONFIG_SITE."* "${S}/exampleTop/configure"
}

do_configure[postfuncs] += "fixup_example_app"