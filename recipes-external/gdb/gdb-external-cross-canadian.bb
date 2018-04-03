require recipes-external/gdb/gdb-external.inc
inherit external-toolchain-cross-canadian

FILES_${PN} = "\
    ${@' '.join('${bindir}/${EXTERNAL_TARGET_SYS}-' + i for i in '${gdb_binaries}'.split())} \
    ${exec_prefix}/${EXTERNAL_TARGET_SYS}/share/gdb \
"
