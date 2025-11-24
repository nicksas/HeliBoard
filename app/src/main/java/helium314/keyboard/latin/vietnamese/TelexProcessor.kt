package helium314.keyboard.latin.vietnamese

import helium314.keyboard.latin.vietnamese.VietnameseUtils.addTone
import helium314.keyboard.latin.vietnamese.VietnameseUtils.getVowelIndex
import helium314.keyboard.latin.vietnamese.VietnameseUtils.removeTone

object TelexProcessor {

    // A map for reversing diacritics, e.g., 'â' -> 'a'
    private val REVERSE_DIACRITIC_MAP = (TelexRules.DOUBLE_TAP_MAP.entries.associate { (k, v) -> v to k }
            + TelexRules.W_MODIFIER_MAP.entries.associate { (k, v) -> v to k })
            .plus(TelexRules.DD to TelexRules.D_MODIFIER)
            .plus(TelexRules.DD.uppercaseChar() to TelexRules.D_MODIFIER.uppercaseChar())


    @JvmStatic
    fun process(currentWord: String, newKey: Char): String? {
        val lowerNewKey = newKey.lowercaseChar()

        // 1. Handle tone markers (RESTORE + APPEND logic)
        if (TelexRules.TONE_MARKERS.containsKey(lowerNewKey)) {
            val tone = TelexRules.TONE_MARKERS.getValue(lowerNewKey)
            val vowelIndex = getVowelIndex(currentWord)
            if (vowelIndex == -1) return null // not a Vietnamese word, do nothing

            val vowelChar = currentWord[vowelIndex]
            val baseVowel = removeTone(vowelChar)

            if (removeTone(vowelChar) != vowelChar) { // has tone
                return if (addTone(baseVowel, tone) == vowelChar) {
                    // Same tone -> remove tone and append the tone key ("mass" behavior)
                    val wordWithToneRemoved = currentWord.replaceRange(vowelIndex, vowelIndex + 1, baseVowel.toString())
                    wordWithToneRemoved + newKey
                } else {
                    // different tone -> replace
                    val newVowel = addTone(baseVowel, tone)
                    currentWord.replaceRange(vowelIndex, vowelIndex + 1, newVowel.toString())
                }
            } else { // no tone
                val newVowel = addTone(vowelChar, tone)
                return currentWord.replaceRange(vowelIndex, vowelIndex + 1, newVowel.toString())
            }
        }

        // 2. Handle tone revert with 'z'
        if (lowerNewKey == TelexRules.TONE_REVERT) {
            var vowelIndex = -1
            var vowelChar = ' '
            for (i in currentWord.indices.reversed()) {
                val char = currentWord[i]
                if (removeTone(char) != char) {
                    vowelIndex = i
                    vowelChar = char
                    break
                }
            }
            if (vowelIndex != -1) { // has tone
                val baseVowel = removeTone(vowelChar)
                return currentWord.replaceRange(vowelIndex, vowelIndex + 1, baseVowel.toString())
            }
            // If no tone, z is just a letter. Let it fall through.
        }

        if (currentWord.isEmpty()) return null

        val lastChar = currentWord.last()
        val lowerLastChar = lastChar.lowercaseChar()

        // 3. Handle "UNDO" for diacritics
        // Case: uow -> ươ -> uow
        if (lowerNewKey == 'w' && currentWord.endsWith("ươ", ignoreCase = true)) {
            val original = if (currentWord.takeLast(2).first().isUpperCase()) "Uo" else "uo"
            return currentWord.substring(0, currentWord.length - 2) + original + "w"
        }
        // Case: aw -> ă -> aw
        val wOriginalKey = TelexRules.W_MODIFIER_MAP.entries.find { it.value == lowerLastChar }?.key
        if (wOriginalKey != null && lowerNewKey == 'w') {
            val originalText = "$wOriginalKey"
            val casedOriginal = if(lastChar.isUpperCase()) originalText.uppercase() else originalText
            return currentWord.substring(0, currentWord.length - 1) + casedOriginal + "w"
        }
        // Case: dd -> đ -> dd
        if (lowerLastChar == 'đ' && lowerNewKey == 'd') {
            val originalText = "dd"
            val casedOriginal = if(lastChar.isUpperCase()) originalText.uppercase() else originalText
            return currentWord.substring(0, currentWord.length - 1) + casedOriginal
        }
        // Case: aa -> â -> aa
        val doubleTapOriginalKey = TelexRules.DOUBLE_TAP_MAP.entries.find { it.value == lowerLastChar }?.key
        if (doubleTapOriginalKey != null && doubleTapOriginalKey == lowerNewKey) {
            val originalText = "$doubleTapOriginalKey$doubleTapOriginalKey"
            val casedOriginal = if(lastChar.isUpperCase() && newKey.isUpperCase()) originalText.uppercase() else originalText
            return currentWord.substring(0, currentWord.length - 1) + casedOriginal
        }


        // 4. Handle standard forward transformations
        // Case: uow -> ươ
        if (lowerNewKey == 'w' && currentWord.endsWith("uo", ignoreCase = true)) {
            val newEnding = if (currentWord.takeLast(2).all { it.isUpperCase() }) "ƯƠ" else if (currentWord.takeLast(2).first().isUpperCase()) "Ươ" else "ươ"
            return currentWord.substring(0, currentWord.length - 2) + newEnding
        }
        // Case: dd -> đ
        if (lowerNewKey == 'd' && lowerLastChar == 'd') {
            val newChar = if (lastChar.isUpperCase() && newKey.isUpperCase()) TelexRules.DD.uppercaseChar() else TelexRules.DD
            return currentWord.substring(0, currentWord.length - 1) + newChar
        }
        // Case: aa -> â, ee -> ê, oo -> ô
        if (TelexRules.DOUBLE_TAP_MAP.containsKey(lowerLastChar) && lowerLastChar == lowerNewKey) {
            val newChar = TelexRules.DOUBLE_TAP_MAP.getValue(lowerLastChar)
            val caseAppropriateNewChar = if (lastChar.isUpperCase() && newKey.isUpperCase()) newChar.uppercaseChar() else newChar
            return currentWord.substring(0, currentWord.length - 1) + caseAppropriateNewChar
        }
        // Case: aw -> ă, ow -> ơ, uw -> ư
        if (lowerNewKey == 'w' && TelexRules.W_MODIFIER_MAP.containsKey(lowerLastChar)) {
            val newChar = TelexRules.W_MODIFIER_MAP.getValue(lowerLastChar)
            val caseAppropriateNewChar = if (lastChar.isUpperCase()) newChar.uppercaseChar() else newChar
            return currentWord.substring(0, currentWord.length - 1) + caseAppropriateNewChar
        }

        return null // No transformation happened
    }
}