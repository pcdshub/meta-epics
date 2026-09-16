
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

This class also handles generating the ``configure/RELEASE.local`` and ``configure/CONFIG_SITE.local`` files, which contain build settings and a list
of EPICS dependencies.

EPICS dependencies should be provided in the ``EPICS_DEPENDS`` variable and are automatically placed into ``configure/RELEASE.local``. To generate the
variable names, the class crawls over the strings in ``EPICS_DEPENDS``, strips the ``epics-`` and ``slac-epics-`` prefixes, converts dashes to underscores,
and uppercases the name. So, ``epics-autosave`` and ``epics-my-module`` become ``AUTOSAVE`` and ``MY_MODULE`` respectively.

Dependencies **must** also appear in the Yocto ``DEPENDS`` variable to get pulled into the recipe's sysroot.

By default, packages are only compiled for the target architecture. If the package contains binaries for the host architecture that are needed to build
for cross compile targets, set ``ENABLE_HOST_PACKAGE=1``.

A summary of all configuration options for ``epics-component`` derived classes can be found below:

+------------------------------+-------------------------------------------------------------------------+
| Variable                     | Description                                                             |
+==============================+=========================================================================+
| ``ENABLE_HOST_PACKAGE``      | Flag indicating whether to build for $EPICS_HOST_ARCH.                  |
|                              |                                                                         |
|                              | Default: 0                                                              |
+------------------------------+-------------------------------------------------------------------------+
| ``EPICS_DEPENDS``            | List of strings defining the list of EPICS dependencies.                |
|                              |                                                                         |
|                              | Default: ""                                                             |
+------------------------------+-------------------------------------------------------------------------+
| ``EPICS_ENABLE_SHARED_LIBS`` | Enable shared libraries for this package. Useful if you need dynamic    |
|                              | loading for something (i.e. pcaspy)                                     |
|                              |                                                                         |
|                              | Default: 0                                                              |
+------------------------------+-------------------------------------------------------------------------+
| ``EPICS_ENABLE_STATIC_LIBS`` | Enable shared libraries for this package. This is enabled by default,   |
|                              | and generally IOCs should be fully statically linked.                   |
|                              |                                                                         |
|                              | This can be enabled at the same time as ``EPICS_ENABLE_SHARED_LIBS``.   |
|                              |                                                                         |
|                              | Default: 1                                                              |
+------------------------------+-------------------------------------------------------------------------+
| ``MODNAME``                  | Defines the module name, which determines how it's installed under      |
|                              | ``/opt/epics``.                                                         |
|                              |                                                                         |
|                              | Default: "${PN}"                                                        |
+------------------------------+-------------------------------------------------------------------------+

epics-module
------------
*Inherits: epics-component*

This class automatically depends on epics-base and epics-base-native, and sanitizes installed ``*.local`` and ``envPaths`` files.

Recipes for EPICS modules should inherit this class.

epics-ioc-systemd
-----------------
*Inherits: epics-module*

EPICS IOCs that are to be deployed on the target using systemd should inherit this bbclass.

This class will automatically install and enable a systemd unit that runs the IOC using procServ.
The IOC's shell will be accessible locally using telnet on the port set in ``PS_PORT``.

Refer to the below table for a list of variables that can be used to control the behavior of this class:

+------------------------------+-------------------------------------------------------------------------+
| Variable                     | Description                                                             |
+------------------------------+-------------------------------------------------------------------------+
| ``PS_PORT``                  | Port to run procServ on.                                                |
|                              |                                                                         |
|                              | Default: 30000                                                          |
+------------------------------+-------------------------------------------------------------------------+
| ``IOC_APP_NAME``             | Name of the IOC application.                                            |
|                              |                                                                         |
|                              | If not provided, the systemd unit relies on the shebang line of the     |
|                              | st.cmd to be configured properly.                                       |
+------------------------------+-------------------------------------------------------------------------+
| ``IOC_PATH``                 | Path to the IOC, relative to the root of the app.                       |
|                              |                                                                         |
|                              | For example: ``iocBoot/ioc-my-test``                                    |
+------------------------------+-------------------------------------------------------------------------+
| ``IOC_ST_CMD``               | Name of the st.cmd file within ``IOC_PATH``.                            |
|                              |                                                                         |
|                              | Default: st.cmd                                                         |
+------------------------------+-------------------------------------------------------------------------+
| ``IOC_ENV``                  | List of additional variables to append to the envPaths                  |
|                              | for this IOC.                                                           |
|                              |                                                                         |
|                              | These variables will be expanded with the suffix of ``_ENV``            |
|                              | when ``envPaths`` is generated.                                         |
|                              |                                                                         |
|                              | For example:                                                            |
|                              |                                                                         |
|                              |     IOC_ENV += "PV_PREFIX"                                              |
|                              |                                                                         |
|                              |     PV_PREFIX_ENV = "SOME:DEVICE:"                                      |
|                              |                                                                         |
|                              | The above code will emit ``epicsEnvSet("PV_PREFIX", "SOME:DEVICE:")``   |
|                              | into the ``envPaths``.                                                  |
|                              |                                                                         |
|                              | This provides a mechanism to pass additional configuration              |
|                              | data to the IOC without patches.                                        |
+------------------------------+-------------------------------------------------------------------------+


Python Infrastructure
=====================

The python library under ``python/epics`` contains some helper functions for interacting with EPICS.
This library is used by the epics-component bbclass (and others) to configure EPICS packages for build.

Library components are available under the ``epics`` package. No imports are necessary.

``target_arch(d) -> str``
-------------------------

Returns the EPICS target architecture for the target. The target architecture can be determined with ``linux-${TARGET_ARCH}`` in a non-Python recipe.

Example: ``linux-aarch64`` for an ARM64 target board.

``host_arch(d) -> str``
-----------------------

Returns the EPICS host architecture. The host architecture can be determined with ``linux-${BUILD_ARCH}`` in a non-Python recipe.

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

