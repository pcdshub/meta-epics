#
# BitBake class for EPICS IOCs that use systemd and procServ.
# Does the following:
#  - Builds and installs the IOC to /opt/epics like other EPICS modules
#  - Installs a systemd unit to start the IOC on system boot using procServ
#

inherit epics-ioc-systemd 

EPICS_DEPENDS += "template-macros"

do_install:append() {
    # Copy child IOC directories
    # Loop shamelessly stolen from epics-component.bbclass and makes it easier
    # to add more directories later if required.
    for d in children; do
        if [ -d $d ]; then
            cp -rfv $d "${D}/opt/epics/${MODNAME}/$d"
        fi
    done
}
