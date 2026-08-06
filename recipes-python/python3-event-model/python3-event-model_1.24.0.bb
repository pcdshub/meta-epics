
inherit pypi python_setuptools_build_meta

SUMMARY = "Data model used by the bluesky ecosystem."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2190357f8c360907663f3e02c7634714"

PYPI_PACKAGE = "event_model"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

RDEPENDS:${PN} += "\
    python3-jsonschema \
    python3-numpy \
    python3-typing-extensions \
    \
"

SRC_URI[sha256sum] = "18e632d52a7caa0987d5d7198bc23e06f2e4ca5e8794d384d74f9e0c34a773d8"

BBCLASSEXTEND = "native nativesdk"
