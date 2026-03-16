# python3-pyqt5_%.bbappend

# PyDM needs these additional packages
PYQT_MODULES += " \
    QtDesigner \
    QtSvg \
    "

DEPENDS += "qttools qtsvg"
