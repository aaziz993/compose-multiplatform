package klib.data.type.primitives.string.tokenization

import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.TokenMatchesSequence
import com.github.h0tk3y.betterParse.lexer.TokenProducer
import com.github.h0tk3y.betterParse.parser.*


public class MapToken<T>(
    public val innerParser: Parser<T>,
    public val transform: (TokenMatch) -> TokenMatch,
) : Parser<T> {
    override fun tryParse(tokens: TokenMatchesSequence, fromPosition: Int): ParseResult<T> =
        innerParser.tryParse(
            TokenMatchesSequence(
                DummyTokenProducer,
                tokens.map(transform).toMutableList() as ArrayList<TokenMatch>
            ), fromPosition
        )
}

public infix fun <A> Parser<A>.mapToken(transform: (TokenMatch) -> TokenMatch): Parser<A> =
    MapToken(this, transform)

private object DummyTokenProducer : TokenProducer {
    override fun nextToken(): TokenMatch? = null
}

