#
# class for all EPICS modules that automatically brings in epics-base and epics-base-native
#

inherit epics-component

# All EPICS modules depend on epics-base and epics-base-native
DEPENDS += "epics-base epics-base-native"
