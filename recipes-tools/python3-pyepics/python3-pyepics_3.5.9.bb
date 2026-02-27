inherit pypi python_setuptools_build_meta

SUMMARY = "PyEpics, a Python library for EPICS"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f113138f4249db9ba9176b8188ac2520"

PYPI_PACKAGE = "pyepics"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "\
    python3-build-native \
    python3-installer-native \
    python3-wheel-native \
    python3-setuptools-native \
    python3-setuptools-scm-native \
    epics-base \
"

RDEPENDS:${PN} += "python3-numpy python3-pyparsing"

SRC_URI[sha256sum] = "78222c1a8aff55bc7a93bdcb6eea9cb544fa8b9122daed1e7ea5b5e87269d45c"

# For some reason python setuptools fails due to the license-files key. Only happens under Yocto...
SRC_URI += "file://001-remote-license-files-pyproject.patch"

BBCLASSEXTEND = "native nativesdk"

do_install:append() {
    # Remove incompatible clibs, this upsets Yocto
    find "${D}${PYTHON_SITEPACKAGES_DIR}/epics/clibs" -iname "*.so" -delete

    # Point pyepics at our libca. For some reason it doesn't do this lookup in $LD_LIBRARY_PATH
    install -d "${D}${sysconfdir}/profile.d"
    echo "export PYEPICS_LIBCA=\"/opt/epics/epics-base/lib/linux-${TARGET_ARCH}/libca.so\"" > "${D}${sysconfdir}/profile.d/pyepics.sh"
}

FILES:${PN} += "${sysconfdir}/profile.d"
