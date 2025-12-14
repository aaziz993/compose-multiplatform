package data.type.primitives.string.tokenization

import klib.data.type.primitives.string.tokenization.combinators.map
import klib.data.type.primitives.string.tokenization.combinators.separated
import klib.data.type.primitives.string.tokenization.combinators.use
import klib.data.type.primitives.string.tokenization.grammar.Grammar
import klib.data.type.primitives.string.tokenization.grammar.parseToEnd
import klib.data.type.primitives.string.tokenization.lexer.regexToken
import klib.data.type.primitives.string.tokenization.lexer.token
import klib.data.type.primitives.string.tokenization.parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class GrammarTest {

    @Test
    fun simpleParse() {
        val digits = "0123456789"
        val g = object : Grammar<Int>() {
            val n by token { input, from ->
                var length = 0
                while (from + length < input.length && input[from + length] in digits)
                    length++
                length
            }
            val s by regexToken("\\+|-")
            val ws by regexToken("\\s+", ignore = true)

            override val rootParser: Parser<Int> = separated(n use { text.toInt() }, s use { text }).map {
                it.reduce { a, s, b ->
                    if (s == "+") a + b else a - b
                }
            }
        }

        val result = g.parseToEnd("1 + 2 + 3 + 4 - 11")
        assertEquals(-1, result)
    }
}
