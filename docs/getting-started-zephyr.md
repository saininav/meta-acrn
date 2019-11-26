Getting Started
---------------

Build acrn sos image having zephyr uos.

Dependencies
------------

This layer depends on:

| meta layer        | git repository                                 | branch       |
|-------------------|------------------------------------------------|--------------|
| poky              | https://git.yoctoproject.org/git/poky          | zeus         |
| meta-intel        | https://git.yoctoproject.org/git/meta-intel    | zeus         |
| meta-acrnl        | https://github.com/saininav/meta-acrn.git      | zephyr       |
| meta-zephyr       | https://github.com/saininav/meta-zephyr.git    | acrn         |



Configuration
-------------

############################################################################################

Append below configuration to local.conf as follows:

```
MACHINE = "intel-corei7-64"
TMPDIR = "${TOPDIR}/master-acrn-sos"
DISTRO = "acrn-demo-sos"
PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"

# Also use the 'uos' & 'zephyr' configuration
BBMULTICONFIG = "uos zephyr"

# The packages-from-images class (container-package.bbclass) needs to know where images are
CONTAINER_PACKAGE_DEPLOY_DIR = "${TOPDIR}/master-acrn-uos/deploy/images/${MACHINE}"
CONTAINER_PACKAGE_ZEPHYR_DEPLOY_DIR = "${TOPDIR}/master-zephyr-app/deploy/images/acrn"
CONTAINER_PACKAGE_MC = "uos"

# Zephyr application to be build & run
ZEPHYR_APP = "zephyr-helloworld"

# Add zephyr-image-base-package to acrn-image-base
IMAGE_INSTALL_append_pn-acrn-image-base = " zephyr-image-package"
```
############################################################################################

Configure `conf/multiconfig/uos.conf` as follows:

```
DISTRO="acrn-demo-uos"
TMPDIR = "${TOPDIR}/master-acrn-uos"
PREFERRED_PROVIDER_virtual/kernel = "acrn-kernel-uos"
```
############################################################################################

Configure `conf/multiconfig/zephyr.conf` as follows:

```
MACHINE= "acrn"
DISTRO = "zephyr"
TMPDIR="${TOPDIR}/master-zephyr-app"
```
############################################################################################

Note: Remember to keep TMPDIR in sync.


Build
-----


Execute below commands sequentially in order to build the sos image shipped with zephyr uos:


```
$ bitbake mc:zephyr:zephyr-helloworld
```
This should build you a `*.elf` in the master-zephyr-app image directory. 
Now prepare zephyr image with grub-efi:


```
$ bitbake mc:uos:zephyr-image
```
This should build you a `zephyr.img` in the uosp image directory. Time to build your acrn image:


```
$ bitbake acrn-image-base
```

Building `acrn-image-base` will build a `wic.acrn` image that on first boot will be normal Linux 
but will setup EFI entries so that subsequent boots are inside ACRN. Alternatively use the EFI 
shell, assuming you've got the image on a USB stick something like this works:

```
> fs1:
> \EFI\BOOT\acrn.efi
```

Run zephyr uos
--------------
```
 $/var/lib/machines/launch_zephyr.sh
```




