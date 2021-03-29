require acrn-common.inc

SRC_URI += "file://dont-build-tools.patch \
            file://allow-to-pass-compiler-and-linker-flags.patch \
            "

inherit python3native

DEPENDS += "util-linux libusb1 openssl libpciaccess acrn-tools"
DEPENDS += "acpica-native"

# Tell the build where to find acrn-tools
EXTRA_OEMAKE += "COPTS=${STAGING_DIR_TARGET}${includedir}/acrn"

PACKAGES += "${PN}-sample"

RDEPENDS_${PN} += "acpica"
RDEPENDS_${PN}-sample += "bash"

FILES_${PN} += "${datadir}/acrn/bios"

do_compile() {
	oe_runmake devicemodel
}

do_install() {
	oe_runmake devicemodel-install

	# Write a modprobe.d so that acrngt is loaded before i915, as otherwise i915
	# fails to initialise and output is disabled.
	install -d ${D}${sysconfdir}/modprobe.d
	echo "softdep i915 pre: acrngt" >${D}${sysconfdir}/modprobe.d/acrn.conf

	# Remove samples, these should be packaged separately.
	rm -rf ${D}${systemd_unitdir}
	rmdir --ignore-fail-on-non-empty `dirname ${D}${systemd_unitdir}`
	rmdir --ignore-fail-on-non-empty ${D}${datadir}
}

FILES_${PN}-sample += "${datadir}/acrn/samples"
