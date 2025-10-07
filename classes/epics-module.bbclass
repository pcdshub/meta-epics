#
# Defines common methods for EPICS module recipes
#

# Default module name is the package name
MODNAME = "${PN}"

# Disable other checks that are incompatible with the EPICS build style
# - staticdev is incompatible with packages that include static libs in the base package.
# - file-rdeps is incompatible with combined sdk/target packages, where we have a mix of host tools and target bins.
INSANE_SKIP:${PN} = "file-rdeps staticdev"

python do_configure() {
    print(f'host arch={epics.host_arch(d)}')
    print(f'epics target={epics.target_arch(d)}')

    print(f'RECIPE_SYSROOT={d.getVar("RECIPE_SYSROOT")}')

    # Generate a RELEASE.local handling all dependencies
    epics.generate_release_local(d)

    # Retarget build products too
    epics.generate_config_site(d)
}

do_compile() {
    echo "Compile deferred until install process..."
}

python do_install() {
    import subprocess
    import os
    
    r = subprocess.run([
        'make',
        f'install.{epics.target_arch(d)}',
        f'-j{os.process_cpu_count()}'
    ])
    
    if r.returncode != 0:
        raise Exception('Build failed')
}

FILES:${PN} += "/opt/epics/${MODNAME}/*"

# Pack together a -dev package so we can expose these files to other recipes
SYSROOT_DIRS += "/opt/epics"
FILES_${PN}-dev += "/opt/epics/${MODNAME}/*"