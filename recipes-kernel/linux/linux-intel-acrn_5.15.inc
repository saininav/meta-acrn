SUMMARY = "Linux Kernel 5.15 with ACRN enabled"

require linux-intel-acrn.inc

SRC_URI:prepend = "git://github.com/intel/linux-intel-lts.git;protocol=https;name=machine;branch=${KBRANCH}; \
                    "

KBRANCH = "5.15/linux"
KMETA_BRANCH = "yocto-5.15"

LINUX_VERSION ?= "5.15.113"
SRCREV_machine ?= "eecb5fe10f0776b67f65f6e4af128422a378151e"
SRCREV_meta ?= "957ddf5f9d4bf5791e88a46ce9ec4352a6d0a171"
