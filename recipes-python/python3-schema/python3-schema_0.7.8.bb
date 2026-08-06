
inherit pypi python_setuptools_build_meta

SUMMARY = "Simple data validation library"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE-MIT;md5=7e70914c99a6ec706baa9fad5e0502df"

PYPI_PACKAGE = "schema"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

SRC_URI[sha256sum] = "e86cc08edd6fe6e2522648f4e47e3a31920a76e82cce8937535422e310862ab5"

BBCLASSEXTEND = "native nativesdk"
