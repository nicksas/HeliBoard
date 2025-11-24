package helium314.keyboard.latin.vietnamese

// Based on https://github.com/dpcuong/vietnamese-telex-keyboard/blob/master/src/core/Telex.js

object TelexRules {
    // Tone markers
    private const val ACUTE = '\u0301'
    private const val GRAVE = '\u0300'
    private const val HOOK = '\u0309'
    private const val TILDE = '\u0303'
    private const val DOT = '\u0323'

    val TONE_MARKERS = mapOf(
        's' to ACUTE,
        'f' to GRAVE,
        'r' to HOOK,
        'x' to TILDE,
        'j' to DOT
    )

    // for reverting tone
    const val TONE_REVERT = 'z'

    // Letter transformations
    val DOUBLE_TAP_MAP = mapOf(
        'a' to 'â',
        'e' to 'ê',
        'o' to 'ô',
    )

    // D is a special case, it will be handled directly in the processor
    const val D_MODIFIER = 'd'
    const val DD = 'đ'

    // w is a special modifier key
    const val W_MODIFIER = 'w'
    val W_MODIFIER_MAP = mapOf(
        'a' to 'ă',
        'o' to 'ơ',
        'u' to 'ư'
    )
}
