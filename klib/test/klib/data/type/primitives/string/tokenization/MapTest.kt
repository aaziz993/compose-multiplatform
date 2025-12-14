package klib.data.type.primitives.string.tokenization
import klib.data.type.primitives.string.tokenization.combinators.*
import klib.data.type.primitives.string.tokenization.lexer.*
import klib.data.type.primitives.string.tokenization.parser.*
import kotlin.test.*

class MapTest {
    val aPlus = RegexToken("aPlus", "a+")
    val bPlus = RegexToken("aPlus", "b+")
    val lexer = DefaultTokenizer(listOf(aPlus, bPlus))

    @Test fun testSuccessfulMap() {
        val tokens = lexer.tokenize("aaa")
        val result = aPlus.map { it.text }.tryParse(tokens,0)
        assertEquals("aaa", result.toParsedOrThrow().value)
    }

    @Test fun testSuccessfulUse() {
        val tokens = lexer.tokenize("abbaaa")
        val result = (aPlus and bPlus and aPlus use { t3.text + t2.text + t1.text}).tryParse(tokens,0)
        assertEquals("aaabba", result.toParsedOrThrow().value)
    }

    @Test fun testSuccessfulAsJust() {
        val tokens = lexer.tokenize("abbaaa")
        val result = (aPlus asJust 1).tryParse(tokens,0)
        assertEquals(1, result.toParsedOrThrow().value)
    }
}
