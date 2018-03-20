require recipes-external/gcc/gcc-external.inc
inherit external-toolchain-cross-canadian

RDEPENDS_${PN} = "binutils-external-cross-canadian-${TRANSLATED_TARGET_ARCH}"
FILES_${PN} = "\
    ${libdir}/gcc/${EXTERNAL_TARGET_SYS} \
    ${libexecdir}/gcc/${EXTERNAL_TARGET_SYS} \
    ${@' '.join('${base_bindir}/${EXTERNAL_TARGET_SYS}-' + i for i in '${gcc_binaries}'.split())} \
"

INSANE_SKIP_${PN} += "dev-so staticdev"
