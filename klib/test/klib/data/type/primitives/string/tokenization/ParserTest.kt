package klib.data.type.primitives.string.tokenization

import klib.data.type.primitives.string.tokenization.lexer.*
import klib.data.type.primitives.string.tokenization.parser.*
import kotlin.test.*

class ParserTest {

    val a = literalToken("a", "a")
    val ignoredX = RegexToken("ignoredX", "x", ignored = true)

    @Test
    fun ignoredUnparsed() {
        val tokens = DefaultTokenizer(listOf(ignoredX, a)).tokenize("axxx")
        a.parseToEnd(tokens) // should not throw
    }

    @Test
    fun unparsedReportsNoIgnoredTokens() {
        val tokens = DefaultTokenizer(listOf(ignoredX, a)).tokenize("axxxa")
        val result = a.tryParseToEnd(tokens, 0)
        assertTrue(result is UnparsedRemainder && result.startsWith.type == a)
    }
}
