@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.primitives.string.tokenization.combinators

import klib.data.type.primitives.string.tokenization.lexer.TokenMatch
import klib.data.type.primitives.string.tokenization.lexer.TokenMatchesSequence
import klib.data.type.primitives.string.tokenization.parser.ErrorResult
import klib.data.type.primitives.string.tokenization.parser.ParseResult
import klib.data.type.primitives.string.tokenization.parser.Parsed
import klib.data.type.primitives.string.tokenization.parser.ParsedValue
import klib.data.type.primitives.string.tokenization.parser.Parser
import klib.data.type.tuples.Tuple2
import kotlin.math.max

public class MapWithMatchesCombinator<T, R>(
    public val innerParser: Parser<T>,
    public val transform: (Tuple2<List<TokenMatch>, T>) -> R
) : Parser<R> {
    override fun tryParse(tokens: TokenMatchesSequence, fromPosition: Int): ParseResult<R> {

        val innerResult = innerParser.tryParse(tokens, fromPosition)
        return when (innerResult) {
            is ErrorResult -> innerResult
            is Parsed -> ParsedValue(
                transform(
                    Tuple2(
                        tokens.toList().subList(fromPosition, max(fromPosition, innerResult.nextPosition)),
                        innerResult.value
                    )
                ),
                innerResult.nextPosition
            )
        }
    }
}

public fun <A, T> Parser<A>.mapWithMatches(
    transform: (Tuple2<List<TokenMatch>, A>) -> T
): Parser<T> = MapWithMatchesCombinator(this, transform)