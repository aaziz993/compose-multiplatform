package klib.data.type.primitives.string.tokenization

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.asJust
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.parser.Parser
import klib.data.type.primitives.string.escape
import klib.data.type.primitives.toNumber

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
        text.substring(1, length - 1).escape('\'')
    }
    public val doubleQuotedString: Parser<String> = Tokens.doubleQuotedString use {
        text.substring(1, length - 1).escape()
    }
}
