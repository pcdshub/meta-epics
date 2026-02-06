#
# BitBake class for EPICS IOCs that use systemd and procServ.
# Does the following:
#  - Builds and installs the IOC to /opt/epics like other EPICS modules
#  - Installs a systemd unit to start the IOC on system boot using procServ
#

inherit epics-ioc-systemd 

EPICS_DEPENDS += "template-macros"
