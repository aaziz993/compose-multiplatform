@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package org.example.klib.data.type.primitives.string.tokenization

import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.TokenMatchesSequence
import com.github.h0tk3y.betterParse.parser.*
import com.github.h0tk3y.betterParse.utils.Tuple2
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