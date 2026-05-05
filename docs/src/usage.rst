
===========
Usage Guide
===========

Classes
=======

An overview of the bitbake classes provided by meta-epics. 

epics-component
---------------

This class defines the tasks necessary to configure, build and install packages built using the EPICS build system. EPICS modules and IOCs should
prefer the epics-module class, which automatically depends on epics-base.
It handles generating the ``configure/RELEASE.local`` and ``configure/CONFIG_SITE.local`` containing the relevant build/configuration settings.

EPICS dependencies are provided in the ``EPICS_DEPENDS`` variable and are automatically placed into ``configure/RELEASE.local``.
Note that these dependencies must also appear in the ``DEPENDS`` variable so they actually get pulled into the sysroot.

epics-module
------------
*Inherits: epics-component*

This class automatically depends on epics-base and epics-base-native. No other behavior is currently included.

Generally speaking, all EPICS modules should inherit this class.

epics-ioc-systemd
-----------------
*Inherits: epics-module*

EPICS IOCs that are to be deployed on the target should use this bbclass.

This class will automatically install and enable a systemd unit that runs the IOC using procServ.
The IOC's shell will be accessible locally using telnet.

Refer to the below table for a list of variables that can be used to control the behavior of this class:

+------------------------------+---------------------------------------------------------+
| Variable                     | Description                                             |
+==============================+=========================================================+
| ``PS_PORT``                  | Port to run procServ on.                                |
|                              | Default: 30000                                          |
+------------------------------+---------------------------------------------------------+
| ``IOC_APP_NAME``             | Name of the IOC application. If not provided, the       |
|                              | systemd unit relies on the shebang line of the st.cmd to|
|                              | be configured properly.                                 |
+------------------------------+---------------------------------------------------------+
| ``IOC_PATH``                 | Path to the IOC, relative to the root of the app.       |
|                              | For example, ``iocBoot/ioc-my-test``                    |
+------------------------------+---------------------------------------------------------+
| ``IOC_ST_CMD``               | Name of the st.cmd file within ``IOC_PATH``.            |
|                              | Default: st.cmd                                         |
+------------------------------+---------------------------------------------------------+

Python Infrastructure
=====================

The python library under ``python/epics`` contains some helper functions for interacting with EPICS.
This library is used by the epics-component bbclass (and others) to configure EPICS packages for build.

Library components are available under the ``epics`` package. No imports are necessary.

``target_arch(d) -> str``
-------------------------

Returns the EPICS target architecture for the target.

Example: ``linux-aarch64`` for an ARM64 target board.

``host_arch(d) -> str``
-----------------------

Returns the EPICS host architecture.

Example: ``linux-x86_64`` for a x86_64 build host.

``generate_config_site(d, extra: dict = {})``
---------------------------------------------

Generates ``configure/CONFIG_SITE.local``, ``configure/CONFIG_SITE.Common.${EPICS_TARGET_ARCH}`` and ``configure/CONFIG_SITE.Common.${EPICS_HOST_ARCH}``
for the package. These files are critical for the cross build to succeed.

``extra`` is a dict of additional key-value pairs to emit into ``CONFIG_SITE.local``

Example:

.. code-block:: python

    epics.generate_config_site(d, {"MYVAR": "YES"})

``generate_release_local(d, extra: dict = {})``
-----------------------------------------------

Generates ``configure/RELEASE.local`` for this package containing a list of dependencies and their paths within the sysroot.

This will overwrite any existing ``RELEASE.local`` files that may be in the repository already.

``extra`` is a dict of additional key-value pairs to emit into ``RELEASE.local``

Example:

.. code-block:: python

    # Alias SEQ to SNCSEQ
    epics.generate_release_local(d, {"SNCSEQ": "$(SEQ)"})

