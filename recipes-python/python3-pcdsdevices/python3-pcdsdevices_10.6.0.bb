inherit pypi python_setuptools_build_meta

SUMMARY = "Ophyd Device definitions for LCLS Beamline components"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2ed04a81f93145a8e913e64266452100"

PYPI_PACKAGE = "pcdsdevices"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

# Note: This recipe skips a number of dependencies that are not expected to
# be used on embedded systems or are not easy to include at the moment, 
# including:
# - happi
# - lightpath
# - pcdscalc
# - scipy
# - pytmc

RDEPENDS:${PN} += "\
    python3-pyyaml \
    python3-pyepics \
    python3-pcdsutils \
    python3-numpy \
    python3-prettytable \
    python3-ophyd \
    python3-jsonschema \
    python3-schema \
    python3-sympy \
    python3-bluesky \
    \
    "

SRC_URI[sha256sum] = "f533222cb58172ae4a96924c89ec10e82b0864a4bdbe3f0c6458d59312c67a58"

BBCLASSEXTEND = "native nativesdk"
