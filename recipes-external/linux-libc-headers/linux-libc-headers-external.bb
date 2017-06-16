require recipes-kernel/linux-libc-headers/linux-libc-headers.inc
inherit external-toolchain

LIC_FILES_CHKSUM = "${COMMON_LIC_CHKSUM}"
DEPENDS = ""
SRC_URI = ""

linux_include_subdirs = "asm asm-generic bits drm linux mtd rdma sound sys video"
FILES_${PN}-dev = "${@' '.join('${includedir}/%s' % d for d in '${linux_include_subdirs}'.split())}"

BBCLASSEXTEND = ""

inherit multilib_header

do_install_extra () {
    oe_multilib_header bits/syscall.h
    if [ -e "${D}${includedir}/bits/long-double.h" ]; then
        oe_multilib_header bits/long-double.h
    fi
}
