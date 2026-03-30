inherit epics-ioc-systemd

SUMMARY = "EPICS Demo IOC recipe"
DESCRIPTION = "Recipe for building a simple demo IOC for EPICS"

LICENSE = "CLOSED"

SRCREV = "9101f7aee512be85ade208c0f1cfcfa2d1b9ce6e"
SRC_URI = "git://github.com/JJL772/epics-yocto-demo-ioc;protocol=https;branch=main;rev=${SRCREV}"

S = "${WORKDIR}/git"

EPICS_DEPENDS += "epics-linstat"
DEPENDS += "${EPICS_DEPENDS}"

# Name of the IOC application (the file in bin/<tarch>/)
IOC_APP_NAME = "systemMonitor"

# Path to the iocBoot directory, where st.cmd lives
IOC_PATH = "iocBoot/sioc-yocto-demo"

# Additional vars to append to the envPaths.
# EPICS_IOC_PV will be set to the value of ${EPICS_IOC_PV_ENV}
IOC_ENV += "EPICS_IOC_PV"

# PV prefix for this IOC. This will be emitted into envPaths.
EPICS_IOC_PV_ENV ?= "SYS:DEMO"
