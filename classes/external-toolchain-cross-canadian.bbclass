inherit external-toolchain cross-canadian

PN .= "-${TRANSLATED_TARGET_ARCH}"

EXTERNAL_INSTALL_SOURCE_PATHS = "${EXTERNAL_TOOLCHAIN}"
FILES_MIRRORS += "\
    ${bindir}/|/bin/\n \
    ${libdir}/|/lib/\n \
    ${libexecdir}/|/libexec/\n \
    ${prefix}/|${target_prefix}/\n \
    ${prefix}/|${target_base_prefix}/\n \
    ${exec_prefix}/|${target_exec_prefix}/\n \
    ${exec_prefix}/|${target_base_prefix}/\n \
    ${base_prefix}/|${target_base_prefix}/\n \
"

# Align with more typical toolchain layout. Everything is already isolated by
# EXTERNAL_TARGET_SYS, we don't need cross-canadian.bbclass to do it for us.
bindir = "${exec_prefix}/bin"
libdir = "${exec_prefix}/lib"
libexecdir = "${exec_prefix}/libexec"

# We're relying on a compatible host libc, not one from a nativesdk build
INSANE_SKIP_${PN} += "build-deps file-rdeps"
