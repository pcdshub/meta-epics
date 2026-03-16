# python3-entrypoints_0.4.bb
inherit pypi python_flit_core

SUMMARY = "The entrypoints module from PyPi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b6f7f5bd22a5dc0397c806c4e5f8cbd3"

BBCLASSEXTEND = "native nativesdk"
PYPI_PACKAGE = "entrypoints"

SRC_URI[sha256sum] = "b706eddaa9218a19ebcd67b56818f05bb27589b1ca9e8d797b74affad4ccacd4"