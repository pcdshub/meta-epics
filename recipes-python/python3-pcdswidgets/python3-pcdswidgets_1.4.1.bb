inherit pypi python_setuptools_build_meta

SUMMARY = "LCLS PyDM Widget Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=13e22dadc02f6af080586b6ed231e922"

PYPI_PACKAGE = "pcdswidgets"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

RDEPENDS:${PN} += "\
    python3-pyyaml \
    python3-pydm \
    python3-qtpy \
    \
    "

SRC_URI[sha256sum] = "287fead7161fd64214833002768e6388db8ec2e009d556b8e9d36ff5a6a51d46"

BBCLASSEXTEND = "native nativesdk"
