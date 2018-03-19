require recipes-external/binutils/binutils-external.inc
inherit external-toolchain-cross-canadian

FILES_${PN} = "\
    ${bindir}/${EXTERNAL_TARGET_SYS}-gdb \
    ${bindir}/${EXTERNAL_TARGET_SYS}-gdbserver \
    ${bindir}/${EXTERNAL_TARGET_SYS}-gdbtiu \
    ${bindir}/${EXTERNAL_TARGET_SYS}-gcore \
    ${exec_prefix}/${EXTERNAL_TARGET_SYS}/share/gdb \
"
