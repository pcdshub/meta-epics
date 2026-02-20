#
# BitBake class for EPICS IOCs that use systemd and procServ.
# Does the following:
#  - Builds and installs the IOC to /opt/epics like other EPICS modules
#  - Installs a systemd unit to start the IOC on system boot using procServ
#

#TODO: Fix generation of systemd unit files for child IOCs
inherit epics-module

EPICS_DEPENDS += "template-macros ioc-common-all"

RDEPENDS += "procserv"

do_install:append() {
    # Copy child IOC directories
    # Loop shamelessly stolen from epics-component.bbclass and makes it easier
    # to add more directories later if required.
    for d in children; do
        if [ -d $d ]; then
            cp -rfv $d "${D}/opt/epics/${MODNAME}/$d"
        fi
    done

    # Sanitize child IOC files
    for i in IOC_APPL_TOP envPaths st.cmd edm*.cmd pydm*.cmd launchgui*.cmd syncts*.cmd *.service start-*.cmd; do
        find "${D}/opt/epics/${MODNAME}" -type f -name $i -exec sed -i "s,${S},/opt/epics/${MODNAME},g" {} \;
        find "${D}/opt/epics/${MODNAME}" -type f -name $i -exec sed -i "s,${RECIPE_SYSROOT},,g" {} \;
    done

    # Copy service files to the correct directory and setup softlinks
    mkdir -p "${D}/etc/systemd/system/multi-user.target.wants"
    find "${D}/opt/epics/${MODNAME}/children" -type f -name "*.service" -exec cp {} "${D}/etc/systemd/system" \;
    for i in $(ls ${D}/etc/systemd/system/*.service); do
        service=$(basename "$i")
        ln -s  "/etc/systemd/system/$service" "${D}/etc/systemd/system/multi-user.target.wants/$service"
    done
}

FILES:${PN} += "/etc/systemd/system"
