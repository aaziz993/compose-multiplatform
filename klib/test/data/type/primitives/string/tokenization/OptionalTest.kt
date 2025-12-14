package data.type.primitives.string.tokenization
import klib.data.type.primitives.string.tokenization.combinators.*
import klib.data.type.primitives.string.tokenization.grammar.*
import klib.data.type.primitives.string.tokenization.lexer.*
import klib.data.type.primitives.string.tokenization.parser.*
import klib.data.type.tuples.Tuple4
import kotlin.test.*

class OptionalTest : Grammar<Nothing>() {
    override val rootParser: Parser<Nothing> get() = throw NoSuchElementException()

    val a by regexToken("a")
    val b by regexToken("b")

    @Test fun successful() {
        val tokens = tokenizer.tokenize("abab")
        val result = optional(a and b and a and b).tryParse(tokens,0)
        assertTrue(result.toParsedOrThrow().value is Tuple4)
    }

    @Test fun unsuccessful() {
        val tokens = tokenizer.tokenize("abab")
        val result = optional(b and a and b and a).tryParse(tokens,0)
        assertNull(result.toParsedOrThrow().value)
    }
}
