package helium314.keyboard.latin.vietnamese

import org.junit.Assert.assertEquals
import org.junit.Test

class TelexProcessorTest {

    // Helper function to simulate typing a word character by character
    private fun typeWord(word: String): String {
        var currentWord = ""
        for (char in word) {
            val result = TelexProcessor.process(currentWord, char)
            currentWord = result ?: (currentWord + char)
        }
        return currentWord
    }

    @Test
    fun testVowelCycle_A() {
        assertEquals("a", typeWord("a"))
        assertEquals("â", typeWord("aa"))
        assertEquals("aa", typeWord("aaa"))
        assertEquals("aaa", typeWord("aaaa"))
    }

    @Test
    fun testVowelCycle_E() {
        assertEquals("e", typeWord("e"))
        assertEquals("ê", typeWord("ee"))
        assertEquals("ee", typeWord("eee"))
        assertEquals("eee", typeWord("eeee"))
    }

    @Test
    fun testVowelCycle_O() {
        assertEquals("o", typeWord("o"))
        assertEquals("ô", typeWord("oo"))
        assertEquals("oo", typeWord("ooo"))
        assertEquals("ooo", typeWord("oooo"))
    }

    @Test
    fun testConsonantCycle_D() {
        assertEquals("d", typeWord("d"))
        assertEquals("đ", typeWord("dd"))
        assertEquals("dd", typeWord("ddd"))
        assertEquals("ddd", typeWord("dddd"))
    }

    @Test
    fun testWModifierCycle_A() {
        assertEquals("ă", typeWord("aw"))
        assertEquals("aw", typeWord("aww"))
        assertEquals("aww", typeWord("awww"))
    }

    @Test
    fun testWModifierCycle_O() {
        assertEquals("ơ", typeWord("ow"))
        assertEquals("ow", typeWord("oww"))
        assertEquals("oww", typeWord("owww"))
    }

    @Test
    fun testWModifierCycle_U() {
        assertEquals("ư", typeWord("uw"))
        assertEquals("uw", typeWord("uww"))
        assertEquals("uww", typeWord("uww"))
    }
    
    @Test
    fun testComplexWCycle_UO() {
        assertEquals("ươ", typeWord("uow"))
        assertEquals("uow", typeWord("uoww"))
        assertEquals("uoww", typeWord("uowww"))
    }

    @Test
    fun testToneRestoreAndAppend() {
        assertEquals("má", typeWord("mas"))
        assertEquals("mas", typeWord("mass"))
        assertEquals("mass", typeWord("masss"))

        assertEquals("bà", typeWord("baf"))
        assertEquals("baf", typeWord("baff"))

        assertEquals("cỏ", typeWord("cor"))
        assertEquals("cor", typeWord("corr"))

        assertEquals("mũ", typeWord("mux"))
        assertEquals("mux", typeWord("muxx"))

        assertEquals("lạ", typeWord("laj"))
        assertEquals("laj", typeWord("lajj"))
    }

    @Test
    fun testToneReplacement() {
        val s1 = typeWord("mas") // má
        val s2 = TelexProcessor.process(s1, 'f') // mà
        assertEquals("mà", s2)
    }

    @Test
    fun testToneZRevert() {
        val s1 = typeWord("mas") // má
        val s2 = TelexProcessor.process(s1, 'z') // ma
        assertEquals("ma", s2)
        
        // z on word with no tone should do nothing special
        assertEquals("maz", typeWord("maz"))
    }

    @Test
    fun testSmartTonePlacement() {
        assertEquals("toán", typeWord("toans"))
        assertEquals("hoà", typeWord("hoaf")) // new orthography
        assertEquals("khỏe", typeWord("khoer")) // new orthography
        assertEquals("quá", typeWord("quas"))
        assertEquals("già", typeWord("giaf"))
    }

    @Test
    fun testNonVietnameseWords() {
        assertEquals("pass", typeWord("pass"))
        assertEquals("hello", typeWord("hello"))
        assertEquals("world", typeWord("world"))
    }
    
    @Test
    fun testMixedCase() {
        assertEquals("Đ", typeWord("DD"))
        assertEquals("dd", typeWord("DDd"))
        assertEquals("Dd", typeWord("Dd")) // should not transform
        
        assertEquals("Â", typeWord("AA"))
        assertEquals("aa", typeWord("AAa"))

        assertEquals("Ă", typeWord("AW"))
        assertEquals("aw", typeWord("AWw"))
    }

    @Test
    fun testEdgeCaseBoong() {
        assertEquals("bô", typeWord("boo"))
        assertEquals("boo", typeWord("booo"))
        assertEquals("boon", typeWord("boon"))
        assertEquals("boong", typeWord("boong"))
    }

    @Test
    fun testEdgeCaseCoop() {
        assertEquals("cô", typeWord("coo"))
        assertEquals("coo", typeWord("cooo"))
        assertEquals("coop", typeWord("coop"))
    }
    
    @Test
    fun testWordNguyen() {
        assertEquals("nguyễn", typeWord("nguyeenx"))
    }
}
