package helium314.keyboard.latin.vietnamese

import java.text.Normalizer

object VietnameseUtils {
    // All Vietnamese vowels
    private val VOWELS = "aăâeêioôơuưyAĂÂEÊIOÔƠUƯY".toSet()
    private val VOWELS_WITHOUT_TONE = "aăâeêioôơuưy"
    private val CHAR_TO_BASE_CHAR = mapOf(
        'á' to 'a', 'à' to 'a', 'ả' to 'a', 'ã' to 'a', 'ạ' to 'a',
        'Á' to 'A', 'À' to 'A', 'Ả' to 'A', 'Ã' to 'A', 'Ạ' to 'A',
        'ắ' to 'ă', 'ằ' to 'ă', 'ẳ' to 'ă', 'ẵ' to 'ă', 'ặ' to 'ă',
        'Ắ' to 'Ă', 'Ằ' to 'Ă', 'Ẳ' to 'Ă', 'Ẵ' to 'Ă', 'Ặ' to 'Ă',
        'ấ' to 'â', 'ầ' to 'â', 'ẩ' to 'â', 'ẫ' to 'â', 'ậ' to 'â',
        'Ấ' to 'Â', 'Ầ' to 'Â', 'Ẩ' to 'Â', 'Ẫ' to 'Â', 'Ậ' to 'Â',
        'é' to 'e', 'è' to 'e', 'ẻ' to 'e', 'ẽ' to 'e', 'ẹ' to 'e',
        'É' to 'E', 'È' to 'E', 'Ẻ' to 'E', 'Ẽ' to 'E', 'Ẹ' to 'E',
        'ế' to 'ê', 'ề' to 'ê', 'ể' to 'ê', 'ễ' to 'ê', 'ệ' to 'ê',
        'Ế' to 'Ê', 'Ề' to 'Ê', 'Ể' to 'Ê', 'Ễ' to 'Ê', 'Ệ' to 'Ê',
        'í' to 'i', 'ì' to 'i', 'ỉ' to 'i', 'ĩ' to 'i', 'ị' to 'i',
        'Í' to 'I', 'Ì' to 'I', 'Ỉ' to 'I', 'Ĩ' to 'I', 'Ị' to 'I',
        'ó' to 'o', 'ò' to 'o', 'ỏ' to 'o', 'õ' to 'o', 'ọ' to 'o',
        'Ó' to 'O', 'Ò' to 'O', 'Ỏ' to 'O', 'Õ' to 'O', 'Ọ' to 'O',
        'ố' to 'ô', 'ồ' to 'ô', 'ổ' to 'ô', 'ỗ' to 'ô', 'ộ' to 'ô',
        'Ố' to 'Ô', 'Ồ' to 'Ô', 'Ổ' to 'Ô', 'Ỗ' to 'Ô', 'Ộ' to 'Ô',
        'ớ' to 'ơ', 'ờ' to 'ơ', 'ở' to 'ơ', 'ỡ' to 'ơ', 'ợ' to 'ơ',
        'Ớ' to 'Ơ', 'Ờ' to 'Ơ', 'Ở' to 'Ơ', 'Ỡ' to 'Ơ', 'Ợ' to 'Ơ',
        'ú' to 'u', 'ù' to 'u', 'ủ' to 'u', 'ũ' to 'u', 'ụ' to 'u',
        'Ú' to 'U', 'Ù' to 'U', 'Ủ' to 'U', 'Ũ' to 'U', 'Ụ' to 'U',
        'ứ' to 'ư', 'ừ' to 'ư', 'ử' to 'ư', 'ữ' to 'ư', 'ự' to 'ư',
        'Ứ' to 'Ư', 'Ừ' to 'Ư', 'Ử' to 'Ư', 'Ữ' to 'Ư', 'Ự' to 'Ư',
        'ý' to 'y', 'ỳ' to 'y', 'ỷ' to 'y', 'ỹ' to 'y', 'ỵ' to 'y',
        'Ý' to 'Y', 'Ỳ' to 'Y', 'Ỷ' to 'Y', 'Ỹ' to 'Y', 'Ỵ' to 'Y',
    )

    fun isVowel(char: Char): Boolean {
        return char in VOWELS
    }

    fun addTone(char: Char, tone: Char): Char {
        val s = char.toString() + tone
        return Normalizer.normalize(s, Normalizer.Form.NFC).first()
    }

    fun removeTone(char: Char): Char {
        return CHAR_TO_BASE_CHAR.getOrDefault(char, char)
    }

    // A simplified algorithm to find the vowel to put tone mark on
    fun getVowelIndex(word: String): Int {
        val lastVowelIndex = word.indexOfLast { isVowel(it) }
        if (lastVowelIndex == -1) return -1

        // "gi" case
        if (word.contains("gi", ignoreCase = true)
            && VOWELS_WITHOUT_TONE.count { word.contains(it, ignoreCase = true) } > 1
            && removeTone(word[lastVowelIndex]).lowercaseChar() == 'i'
        ) {
            val iIndex = word.lastIndexOf('i', ignoreCase = true)
            if (iIndex > 0 && word[iIndex - 1].lowercaseChar() == 'g') {
                return word.substring(0, iIndex).indexOfLast { isVowel(it) }
            }
        }
        
        // qu case, u is not a vowel here
        if (lastVowelIndex > 0 && word[lastVowelIndex].lowercaseChar() == 'u' && word[lastVowelIndex - 1].lowercaseChar() == 'q') {
            return word.substring(0, lastVowelIndex).indexOfLast { isVowel(it) }
        }

        //
        val precedingVowelIndex = if (lastVowelIndex > 0) word.substring(0, lastVowelIndex).indexOfLast { isVowel(it) } else -1
        if (precedingVowelIndex != -1) {
            val lastVowel = removeTone(word[lastVowelIndex]).lowercaseChar()
            val precedingVowel = removeTone(word[precedingVowelIndex]).lowercaseChar()
            // Diphthongs that get tone on the first vowel
            if ("eou".contains(precedingVowel) && "iy".contains(lastVowel)) return precedingVowelIndex
            if (precedingVowel == 'a' && "iy".contains(lastVowel)) return precedingVowelIndex
            if (precedingVowel == 'ơ' && 'i' == lastVowel) return precedingVowelIndex
            // Diphthongs that get tone on the second vowel
            if (precedingVowel == 'u' && "ye".contains(lastVowel)) return lastVowelIndex
            // special 'ia', 'ua', 'ưa' cases, get tone on first vowel
            if ("iư".contains(precedingVowel) && lastVowel == 'a') return precedingVowelIndex
            if (precedingVowel == 'u' && lastVowel == 'a' && precedingVowelIndex > 0 && isVowel(word[precedingVowelIndex -1])) return lastVowelIndex
            if (precedingVowel == 'u' && lastVowel == 'a') return precedingVowelIndex
        }

        return lastVowelIndex
    }
}
