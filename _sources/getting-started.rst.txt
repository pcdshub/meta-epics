
===============
Getting Started
===============

Getting meta-epics
==================

meta-epics can be cloned from GitHub at: https://github.com/pcdshub/meta-epics.git

The layer can be added to your Yocto project using bitbake-layers:

.. code-block:: bash
   
   git clone git@github.com:pcdshub/meta-epics.git
   bitbake-layers add-layer meta-epics


To install the EPICS base tools (caget, caput, etc.) into your image, add the following to your local.conf

.. code-block::

   IMAGE_INSTALL:append = " epics-base"


The resulting image should now have the EPICS base tools installed to /usr/local/bin and available in your $PATH.
The EPICS base files are located at /opt/epics/epics-base.

Running a Demo IOC
==================

The recipe ``epics-demo-ioc`` (located in ``recipes-examples/demo-ioc``) provides a simple system monitoring IOC using the linStat EPICS module.

To install this IOC on your target, add the following to your ``local.conf``:

.. code-block::
   
   IMAGE_INSTALL:append = " epics-demo-ioc"

After rebuilding your image and uploading it to the target, you should be able to access iocsh using telnet:

.. code-block::

   $ telnet localhost 30000
   Connected to localhost
   @@@ Welcome to procServ (procServ Process Server 2.8.0)
   @@@ Use ^X to kill the child, auto restart mode is ON, use ^T to toggle auto restart
   @@@ procServ server PID: 548
   @@@ Server startup directory: /
   @@@ Child startup directory: /
   @@@ Child started as: /opt/epics/epics-demo-ioc/ioc-start.sh
   @@@ Child log file: -
   @@@ Child "/opt/epics/epics-demo-ioc/ioc-start.sh" PID: 579
   @@@ procServ server started at: Wed Jan  8 18:23:03 2025
   @@@ Child "/opt/epics/epics-demo-ioc/ioc-start.sh" started at: Wed Jan  8 18:23:04 2025
   @@@ 0 user(s) and 0 logger(s) connected (plus you)

   Entering character mode
   Escape character is '^]'.

   epics> echo hello
   hello

PVs on the demo IOC are accessible with PVAccess and ChannelAccess:

.. code-block::
   
   $ pvlist 
   GUID 0x88C27E6700000000A0D88232 version 2: tcp@[ 10.0.0.197:5075 ]

   $ pvlist 0x88C27E6700000000A0D88232
   SYS:DEMO:CAP_EFF_N
   SYS:DEMO:IRQ_TOTR_
   SYS:DEMO:SYS_UP
   ...
   
   $ pvget SYS:DEMO:SYS_UP
   SYS:DEMO:SYS_UP 2025-01-22 10:04:10.052  1.17969e+06

   $ caget SYS:DEMO:SYS_UP
   SYS:DEMO:SYS_UP               1.1797e+06

Assuming your ``EPICS_CA_ADDR_LIST`` and ``EPICS_PVA_ADDR_LIST`` are configured properly on a remote machine, they will be accessible there too.