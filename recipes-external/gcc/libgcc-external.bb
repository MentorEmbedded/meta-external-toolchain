SUMMARY = "The GNU Compiler Collection - libgcc"
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
DEPENDS += "virtual/${TARGET_PREFIX}binutils"
PV = "${GCC_VERSION}"

inherit external-toolchain

LICENSE = "GPL-3.0-with-GCC-exception"

# libgcc needs libc, but glibc's utilities need libgcc, so short-circuit the
# interdependency here by manually specifying it rather than depending on the
# libc packagedata.
RDEPENDS_${PN} += "${@'${PREFERRED_PROVIDER_virtual/libc}' if '${PREFERRED_PROVIDER_virtual/libc}' else '${TCLIBC}'}"
INSANE_SKIP_${PN} += "build-deps file-rdeps"

# The dynamically loadable files belong to libgcc, since we really don't need the static files
# on the target, moreover linker won't be able to find them there (see original libgcc.bb recipe).
BINV = "${GCC_VERSION}"
FILES_${PN} = "${base_libdir}/libgcc_s.so.*"
FILES_${PN}-dev = "${base_libdir}/libgcc_s.so \
                   ${libdir}/${EXTERNAL_TARGET_SYS}/${BINV}* \
                   "
INSANE_SKIP_${PN}-dev += "staticdev"
FILES_${PN}-dbg += "${base_libdir}/.debug/libgcc_s.so.*.debug"

do_install_extra () {
    if [ -e "${D}${libdir}/${EXTERNAL_TARGET_SYS}" ]; then
        if ! [ -e "${D}${libdir}/${TARGET_SYS}" ]; then
            ln -s "${EXTERNAL_TARGET_SYS}" "${D}${libdir}/${TARGET_SYS}"
        fi
    fi
}

do_package[prefuncs] += "add_sys_symlink"

python add_sys_symlink () {
    import pathlib
    target_sys = pathlib.Path(d.expand('${D}${libdir}/${TARGET_SYS}'))
    if target_sys.exists():
        pn = d.getVar('PN')
        d.appendVar('FILES_%s-dev' % pn, ' ${libdir}/${TARGET_SYS}')
}
