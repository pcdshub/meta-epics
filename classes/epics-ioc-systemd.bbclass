#
# BitBake class for EPICS IOCs that use systemd and procServ.
# Does the following:
#  - Builds and installs the IOC to /opt/epics like other EPICS modules
#  - Installs a systemd unit to start the IOC on system boot using procServ
#

inherit epics-module

# procServ is used to manage the IOCs, we'll also need telnet to access the IOC later
DEPENDS += "procserv"
RDEPENDS:${PN} += "procserv"

# --- User provided settings --- #

# Port to run procServ on
PS_PORT ?= "30000"

# IOC application name (located within this package's bin/<arch>)
# If left empty, it will rely on the shebang line in the st.cmd
IOC_APP_NAME ?= ""

# Path to the IOC, relative to the package root. ex: iocBoot/sioc-my-example
IOC_PATH ?= ""

# List of additional variable names to append to envPaths for the IOC.
# These will be expanded with the suffix _ENV. So, IOC_ENV += "PV_PREFIX" will be set to the value of ${PV_PREFIX_ENV} in Yocto.
IOC_ENV ?= ""

# Name of the IOC's st.cmd (usually just st.cmd)
IOC_ST_CMD ?= "st.cmd"

# --- End user provided settings --- #

#update_env_paths() {
#    F="${D}/opt/epics/${MODNAME}/${IOC_PATH}/envPaths"
#    for e in ${IOC_ENV}; do
#        eval "V=\${${e}_ENV}"
#        echo "epicsEnvSet(\"${e}\", \"${V}\")" >> "${F}"
#    done
#}

python update_env_paths() {
    D = d.getVar("D")
    MODNAME = d.getVar("MODNAME")
    IOC_PATH = d.getVar("IOC_PATH")

    import os
    with open(f"{D}/opt/epics/{MODNAME}/{IOC_PATH}/envPaths", "a") as fp:
        for k in d.getVar("IOC_ENV").split(" "):
            if len(k) == 0: continue
            V = d.getVar(f"{k}_ENV")
            if V is None: V = ""
            fp.write(f"epicsEnvSet(\"{k}\", \"{V}\")\n")
}

# Installs a systemd unit to automatically start the IOC
install_systemd_unit() {
    U="${D}/etc/systemd/system/${PN}.service"
    SHS="${D}/opt/epics/${MODNAME}/ioc-start.sh"

    mkdir -p "$(dirname "${U}")"

    # Ensure the st.cmd is actually executable (we may exec with ./)
    chmod +x "${D}/opt/epics/${MODNAME}/${IOC_PATH}/${IOC_ST_CMD}"

    # Generate a shell script with the launch commands
    echo "#!/usr/bin/env bash" >> ${SHS}
    echo "set -e" >> ${SHS}
    echo "cd \"/opt/epics/${MODNAME}/${IOC_PATH}\"" >> ${SHS}
    if [ -z "${IOC_APP_NAME}" ]; then
        echo "./${IOC_ST_CMD}" >> ${SHS}
    else
        echo "/opt/epics/${MODNAME}/bin/linux-${TARGET_ARCH}/${IOC_APP_NAME} ${IOC_ST_CMD}" >> ${SHS}
    fi
    
    # Make sure it's executable...
    chmod +x "${D}/opt/epics/${MODNAME}/ioc-start.sh"

    # Generate the actual systemd unit
    echo "[Unit]" >> ${U}
    echo "Description=${PN} IOC Unit" >> ${U}
    echo "After=network.target" >> ${U}
    echo "[Service]" >> ${U}
    echo "Type=simple" >> ${U}
    echo "ExecStart=procServ -f -L - -P ${PS_PORT} /opt/epics/${MODNAME}/ioc-start.sh" >> ${U}
    echo "Restart=on-failure" >> ${U}
    echo "RestartSec=5s" >> ${U}
    echo "[Install]" >> ${U}
    echo "WantedBy=multi-user.target" >> ${U}
    
    chmod 644 ${U}
    
    # Force the unit to start on boot
    mkdir -p ${D}/etc/systemd/system/multi-user.target.wants
    ln -s "/etc/systemd/system/${PN}.service" "${D}/etc/systemd/system/multi-user.target.wants/${PN}.service"
}

do_install[postfuncs] += "update_env_paths install_systemd_unit"

FILES:${PN} += "/opt/epics/${MODNAME}/ioc-start.sh"
FILES:${PN} += "/etc/systemd/system/${PN}.service"
FILES:${PN} += "/etc/systemd/system/multi-user.target.wants/${PN}.service"
