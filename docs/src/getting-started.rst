
===============
Getting Started
===============

meta-epics can be cloned from https://github.com/pcdshub/meta-epics.git

The layer can be added to your Yocto project using bitbake-layers:

.. code-block:: bash
   
   bitbake-layers add-layer meta-epics


To install the EPICS base tools (caget, caput, etc.) into your image, add the following to your local.conf

.. code-block::

   IMAGE_INSTALL:append = " epics-base"


The resulting image should now have the EPICS base tools installed to /usr/local/bin and available in your $PATH.
The EPICS base files are located at /opt/epics/epics-base.


