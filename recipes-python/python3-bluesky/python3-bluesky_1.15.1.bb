inherit pypi python_setuptools_build_meta

SUMMARY = "Experiment specification & orchestration"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d2c5026d95adab3a56e79a361afc919"

PYPI_PACKAGE = "bluesky"

PEP517_BUILD_API = "setuptools.build_meta"

DEPENDS += "python3-tomli-native python3-setuptools-scm-native"

RDEPENDS:${PN} += "\
    python3-numpy \
    python3-opentelemetry-api \
    python3-cycler \
    python3-event-model \
    python3-typing-extensions \
    python3-msgpack \
    python3-msgpack-numpy \
    python3-tqdm \
    python3-toolz \
    \
    "

SRC_URI[sha256sum] = "956e161a8f34f698af28edcd501db118fa793a6c43241001e30ed8c6aaabd95c"

BBCLASSEXTEND = "native nativesdk"
