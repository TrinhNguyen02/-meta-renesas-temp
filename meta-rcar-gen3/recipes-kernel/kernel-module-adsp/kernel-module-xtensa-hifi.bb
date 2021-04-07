DESCRIPTION = "ADSP Driver for Linux"
LICENSE = "GPLv2 & MIT"

LIC_FILES_CHKSUM = " \
    file://COPYING;md5=55979d94ccbb973fdea772250e0c54d6 \
    file://MIT-COPYING;md5=f932f6ad0feea4f97b6e8316e172070e \
"

inherit module features_check

REQUIRED_DISTRO_FEATURES = "adsp"

FILESEXTRAPATHS_prepend := "${THISDIR}/xtensa-hifi:"

SRC_URI = "file://RTM8RC0000ZNA2DS00JFL3E.tar.gz"

S = "${WORKDIR}/RTM8RC0000ZNA2DS00JFL3E"

SRC_URI_append = " \
    file://update-adsp-alsa.c-RTM8RC0000ZNA2DS00JFL3E.patch;patchdir=kernel-source/sound/soc/adsp/ \
    file://update-xf-rcar.c-RTM8RC0000ZNA2DS00JFL3E.patch;patchdir=src \
"

# Define the extra config for using module.bbclass to build ADSP driver
EXTRA_OEMAKE_append = " KDIR=${STAGING_KERNEL_DIR}"

# Build ADSP kernel module without suffix
KERNEL_MODULE_PACKAGE_SUFFIX = ""

# Enable build target for E3 board
do_configure_prepend_r8a77990(){
    sed -i 's|-DTARGET_BOARD_E3=0|-DTARGET_BOARD_E3=1|g' ${S}/Kbuild
}

do_install_append(){
    # Create install directories
    install -d ${D}/${includedir}
    install -d ${D}/${includedir}/sys/xt-shmem

    # Install shared header files
    install -m 644 ${S}/include/*.h ${D}/${includedir}/
    install -m 644 ${S}/include/sys/xt-shmem/*.h ${D}/${includedir}/sys/xt-shmem/
}

# The ADSP driver need adsp firmware for running
RDEPENDS_${PN}_append = " adsp-fw-module"
