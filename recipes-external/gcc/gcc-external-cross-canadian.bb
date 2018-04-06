require recipes-external/gcc/gcc-external.inc
inherit external-toolchain-cross-canadian

BINV = "${GCC_VERSION}"

RDEPENDS_${PN} = "binutils-external-cross-canadian-${TRANSLATED_TARGET_ARCH}"
FILES_${PN} = "\
    ${libdir}/gcc/${EXTERNAL_TARGET_SYS}/${BINV} \
    ${libexecdir}/gcc/${EXTERNAL_TARGET_SYS}/${BINV} \
    ${@' '.join('${base_bindir}/${EXTERNAL_TARGET_SYS}-' + i for i in '${gcc_binaries}'.split())} \
"

INSANE_SKIP_${PN} += "dev-so staticdev"
