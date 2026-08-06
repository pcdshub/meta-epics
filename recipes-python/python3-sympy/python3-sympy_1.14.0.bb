
inherit pypi python_setuptools_build_meta

SUMMARY = "Computer algebra system (CAS) in Python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ea48085d7dff75b49271b25447e8cdca"

PYPI_PACKAGE = "sympy"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

SRC_URI[sha256sum] = "d3d3fe8df1e5a0b42f0e7bdf50541697dbe7d23746e894990c030e2b05e72517"

BBCLASSEXTEND = "native nativesdk"
