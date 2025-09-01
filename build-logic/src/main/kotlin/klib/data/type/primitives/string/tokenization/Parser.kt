package org.example.klib.data.type.primitives.string.tokenization

import com.github.h0tk3y.betterParse.lexer.CharToken
import com.github.h0tk3y.betterParse.lexer.Token
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.TokenMatchesSequence
import com.github.h0tk3y.betterParse.lexer.TokenProducer
import com.github.h0tk3y.betterParse.parser.ParseResult
import com.github.h0tk3y.betterParse.parser.Parser


public fun <T> Parser<T>.mapToken(transform: (TokenMatch) -> TokenMatch): Parser<T> = object : Parser<T> {
    @Suppress("UNCHECKED_CAST")
    override fun tryParse(
        tokens: TokenMatchesSequence,
        fromPosition: Int
    ): ParseResult<T> = this@mapToken.tryParse(
        TokenMatchesSequence(
            DummyTokenProducer,
            tokens.map(transform).toMutableList() as ArrayList<TokenMatch>
        ), fromPosition
    )
}


private object DummyTokenProducer : TokenProducer {
    override fun nextToken(): TokenMatch? = throw UnsupportedOperationException()
}