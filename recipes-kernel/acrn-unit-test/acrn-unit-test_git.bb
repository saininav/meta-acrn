SUMMARY = "Unit test for ACRN"
HOMEPAGE = "https://github.com/projectacrn/acrn-unit-test"
LICENSE = "BSD & GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://../COPYRIGHT;md5=3aea0500d77618be157275ec00e4e005 \
                    file://COPYRIGHT;md5=8a44f57fb36dd391ae65e11a6d370615 \
                    "

SRC_URI = "git://github.com/projectacrn/acrn-unit-test.git \
        file://0001-Makefile-ensure-cross-compilation-env-is-used.patch;patchdir=.. \
        "

SRCREV = "8b8e0439435ce0c3cac612f196a6d184a27a7b1c"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git/guest"

inherit pkgconfig

CFLAGS += " -fno-pic -no-pie -fno-omit-frame-pointer -mno-red-zone -mno-sse -mno-sse2 "
RDEPENDS_${PN} = " bash coreutils util-linux tar gzip bzip2 python kmod qemu "

do_configure() {
    ./configure --arch=${TARGET_ARCH} --cross-prefix=${TARGET_PREFIX} --prefix=${D}${prefix}

    # acrn-unit-test does not use env CFLAGS
    # vmx_test look for debugreg.h in ${S}/lib/x86/asm/ where it is not preset
    # so we copy it from the sysroot to the acrn-unit-test lib/x86/asm
    install -D -m0644 ${STAGING_INCDIR}/asm/debugreg.h ${S}/lib/x86/asm/

}

do_install() {
    install -d ${D}${datadir}/${BPN}
    install -d ${D}${datadir}/${BPN}/x86
    install -m 0755 ${S}/x86/*.flat   ${D}${datadir}/${BPN}/x86/
    install -m 0755 ${S}/x86/run  ${D}${datadir}/${BPN}/x86/
    install -m 0755 ${S}/run_tests.sh ${D}${datadir}/${BPN}/
    cp ${S}/config.mak ${D}${datadir}/${BPN}/
    cp ${S}/build-head ${D}${datadir}/${BPN}/
    cp -r ${S}/scripts   ${D}${datadir}/${BPN}/
    cp ${S}/x86/unittests.cfg  ${D}${datadir}/${BPN}/x86/
    rm -rf ${D}${datadir}/${BPN}/x86/.debug
}


#do_package_qa: QA Issue: Architecture did not match (x86, expected x86-64) on ../packages-split/kvm-unit-tests/usr/share/kvm-unit-tests/x86/smap.flat
# %.flat: %.elf
#    $(OBJCOPY) -O elf32-i386 $^ $@

INHIBIT_PACKAGE_DEBUG_SPLIT="1"
INSANE_SKIP_${PN} += " arch "
