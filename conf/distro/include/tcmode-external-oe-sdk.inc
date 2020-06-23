require conf/distro/include/tcmode-external.inc

TCMODEOVERRIDES .= ":tcmode-external-oe-sdk"

EXTERNAL_TARGET_SYS ?= "${TARGET_SYS}"

EXTERNAL_SDK_SYS ?= "${SDK_SYS}"
EXTERNAL_TOOLCHAIN_BIN = "${EXTERNAL_TOOLCHAIN}/sysroots/${EXTERNAL_SDK_SYS}/usr/bin/${EXTERNAL_TARGET_SYS}"
EXTERNAL_MULTIMACH_TARGET_SYS ?= "${MULTIMACH_TARGET_SYS}"
EXTERNAL_TOOLCHAIN_SYSROOT = "${EXTERNAL_TOOLCHAIN}/sysroots/${EXTERNAL_MULTIMACH_TARGET_SYS}"

EXTERNAL_CC = "${EXTERNAL_TARGET_SYS}-gcc --sysroot=${EXTERNAL_TOOLCHAIN_SYSROOT}"
EXTERNAL_TOOLCHAIN_FEATURES_append = " locale-utf8-is-default"

# Our headers are already multilib
oe_multilib_header_pn-glibc-external = ":"

python extract_env_setup_metadata() {
    import pathlib
    import subprocess

    external_toolchain = pathlib.Path(d.getVar('EXTERNAL_TOOLCHAIN'))
    if external_toolchain.is_absolute() and external_toolchain.is_dir():
        setup = d.getVar('EXTERNAL_TOOLCHAIN_SETUP_SCRIPT')
        if setup:
            setup = pathlib.Path(setup)
        else:
            arch = d.getVar('TUNE_PKGARCH')
            setups = external_toolchain.glob('environment-setup-' + arch + '-*')
            if not setups:
                bb.fatal('tcmode-external-oe-sdk: failed to determine setup script path for sdk at {}, please set EXTERNAL_TOOLCHAIN_SETUP_SCRIPT to the full path to the environment setup script.'.format(external_toolchain))
            else:
                setups = list(setups)
                if len(setups) > 1:
                    bb.fatal('tcmode-external-oe-sdk: multiple setup scripts available at {}, please specify the path in EXTERNAL_TOOLCHAIN_SETUP_SCRIPT'.format(external_toolchain))
                else:
                    setup = setups[0]

        env = parse_setup_script(setups[0])
        target_sys = env.get('TARGET_PREFIX')[:-1]
        native_sysroot = pathlib.Path(env.get('OECORE_NATIVE_SYSROOT'))

        d.setVar('EXTERNAL_TARGET_SYS', str(target_sys))
        d.setVar('EXTERNAL_TOOLCHAIN_BIN', str(native_sysroot / 'usr' / 'bin' / target_sys))
        d.setVar('EXTERNAL_TOOLCHAIN_SYSROOT', env.get('SDKTARGETSYSROOT'))
}
extract_env_setup_metadata[eventmask] = "bb.event.ConfigParsed"
addhandler extract_env_setup_metadata

def parse_setup_script(setup):
    import shlex
    import subprocess

    with open(setup, 'r') as f:
        value = f.read()

    values = {}
    for line in value.splitlines():
        if line.split():
            split = shlex.split(line)
            if split and split[0] == 'export':
                k, v = split[1].split('=', 1)
                values[k] = v
    return values
