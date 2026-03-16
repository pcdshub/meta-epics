# pydm_1.28.2.bb

# To launch PyDM on a target without a window manager/desktop environment, you can use the Qt VNC plugin:
#   $ QT_QPA_PLATFORM=vnc pydm
# And then connect to the given IP and port number with your favorite VNC client
# NOTE: you may need to install fonts (i.e. liberation-fonts) if you're installing PyDM onto a headless system w/o a window manager.

# TODO:
#  - Once a p4p recipe is finalized, add it here so we get the p4p plugin

inherit pypi python_setuptools_build_meta

SUMMARY = "PyDM, a Python display manager for EPICS"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=37b6609b3fc70c8026a99b694dee7714"

PYPI_PACKAGE = "pydm"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

RDEPENDS:${PN} += "\
    python3-pyqt5 \
    python3-numpy \
    python3-pyyaml \
    python3-pyepics \
    python3-qtpy \
    python3-requests \
    python3-six \
    python3-entrypoints \
    python3-pyqtgraph \
    "

SRC_URI[sha256sum] = "1768a06997686bd4d3d899e52047054452646a2381e2c495719bf710fe45ab92"

BBCLASSEXTEND = "native nativesdk"

# Getting some errors related to the 'tool.setuptools.dynamic.optional-dependencies.test-no-optional' key.
# Setuptools doesn't seem to like the test-no-optional part; no idea why this only happens under Yocto
SRC_URI += "file://001-pydm-pyproject-toml-fix.patch"
