package klib.data.type.primitives.string.tokenization.parser

import klib.data.type.primitives.string.tokenization.combinators.and
import klib.data.type.primitives.string.tokenization.combinators.asJust
import klib.data.type.primitives.string.tokenization.combinators.map
import klib.data.type.primitives.string.tokenization.combinators.oneOrMore
import klib.data.type.primitives.string.tokenization.combinators.optional
import klib.data.type.primitives.string.tokenization.combinators.or
import klib.data.type.primitives.string.tokenization.combinators.separatedTerms
import klib.data.type.primitives.string.tokenization.combinators.times
import klib.data.type.primitives.string.tokenization.combinators.unaryMinus
import klib.data.type.primitives.string.tokenization.combinators.use
import klib.data.type.primitives.string.tokenization.lexer.TokenMatch
import klib.data.type.primitives.string.tokenization.lexer.Tokens
import klib.data.type.primitives.string.unescape
import klib.data.type.primitives.number.toInt
import klib.data.type.primitives.number.toNumber

public object Parsers {
    // Literals.
    public val `null`: Parser<Any?> = Tokens.`null` asJust null
    public val boolean: Parser<Boolean> = Tokens.boolean use { text.toBoolean() }
    private val sign = Tokens.plus or Tokens.hyphen
    private val exponentPart = (-Tokens.exponent * optional(sign) * Tokens.integer).map { (s, exp) ->
        "e${s?.text.orEmpty()}${exp.text}"
    }
    private val floatingPointPart = (-Tokens.period * optional(Tokens.integer)).map { frac ->
        ".${frac?.text.orEmpty()}"
    }
    private val onlyFloatingPart = (-Tokens.period * Tokens.integer).map { frac -> ".${frac.text}" }
    public val positiveNumber: Parser<String> =
        ((Tokens.integer * optional(floatingPointPart)).map { (int, frac) -> "${int.text}${frac.orEmpty()}" } or
                onlyFloatingPart) and optional(exponentPart) map { (mantissa, exp) -> "$mantissa${exp.orEmpty()}" }
    public val number: Parser<Any> = (optional(Tokens.hyphen) * positiveNumber * optional(Tokens.numberSuffix))
        .map { (minus, num, suffix) -> "${minus?.text.orEmpty()}$num${suffix?.text.orEmpty()}".toNumber() }
    public val character: Parser<Char> =
        Tokens.character.map { tok ->
            val body = tok.text.substring(1, tok.text.lastIndex) // inside quotes
            when {
                body.length == 1 && body[0] != '\\' -> body[0]
                body.startsWith("\\") -> when (body.getOrNull(1)) {
                    'n' -> '\n'; 'r' -> '\r'; 't' -> '\t'
                    'b' -> '\b'; 'f' -> '\u000C'
                    '\\' -> '\\'; '\'' -> '\''
                    else -> body.getOrNull(1) ?: '\uFFFD'
                }

                else -> body.firstOrNull() ?: '\uFFFD'
            }
        }
    public val singleQuotedString: Parser<String> = Tokens.doubleQuotedString use {
        text.substring(1, length - 1).unescape('\'')
    }
    public val doubleQuotedString: Parser<String> = Tokens.doubleQuotedString use {
        text.substring(1, length - 1).unescape()
    }

    // Id.
    public val id: Parser<String> = Tokens.id use TokenMatch::text

    // Reference.
    public val key: Parser<Any?> = `null` or
            Tokens.integer.use { text.toInt() } or
            oneOrMore(Tokens.id or Tokens.integer or Tokens.hyphen use TokenMatch::text).use { joinToString("") } or
            (-Tokens.leftSqBr * Tokens.integer * -Tokens.rightSqBr).use { text.toInt() } or
            Tokens.doubleQuotedString.use { text.substring(1, length - 1) }
    public val path: Parser<List<Any?>> = separatedTerms(key, Tokens.period)
    public val reference: Parser<List<Any?>> = -Tokens.dollar * path
    public val reference_braced: Parser<List<Any?>> =
        -Tokens.dollar * -Tokens.leftBr * path * -Tokens.rightBr
}
