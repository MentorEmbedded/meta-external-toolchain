do_compile_append () {
    sed -i -e 's#-B${gcc_bindir}##' support/bash.pc
}
