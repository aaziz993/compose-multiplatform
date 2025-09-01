package klib.data.type.primitives.string.tokenization.substitution

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import klib.data.type.primitives.string.tokenization.Tokens
import klib.data.type.primitives.string.tokenization.evaluation.program.ProgramGrammar
import org.example.klib.data.type.primitives.string.tokenization.mapToken

public fun String.substitute(
    vararg options: SubstituteOption = arrayOf(
        SubstituteOption.INTERPOLATE_BRACES,
        SubstituteOption.DEEP_INTERPOLATION,
        SubstituteOption.ESCAPE_INTERPOLATION,
        SubstituteOption.EVALUATE,
        SubstituteOption.ESCAPE_EVALUATION
    ),
    getter: (path: List<String>) -> Any?
): Any? = TemplateGrammar(options.toSet(), getter).parseToEnd(this)

@Suppress("UNUSED")
public class TemplateGrammar(
    options: Set<SubstituteOption>,
    getter: (path: List<String>) -> Any?
) : Grammar<Any?>() {
    private val dollarsEscaper: (String) -> String =
        if (SubstituteOption.ESCAPE_INTERPOLATION in options) { dollars -> dollars.substring(0, dollars.length / 2) }
        else { dollars -> dollars }

    private val evaluateEscaper: (String) -> String =
        if (SubstituteOption.ESCAPE_EVALUATION in options) { backslashes ->
            backslashes.substring(0, backslashes.length / 2)
        } else { backslashes -> backslashes }

    private val valueGetter: (path: List<String>) -> Any? =
        if (SubstituteOption.DEEP_INTERPOLATION in options) { path ->
            getter(path)?.let { value -> if (value is String) parseToEnd(value) else value }
        }
        else getter

    // Whitespace and newline tokens.
    private val wsToken by Tokens.ws
    private val nlToken by Tokens.nl

    // Symbol tokens.
    private val leftParToken by Tokens.leftPar
    private val rightParToken by Tokens.rightPar
    private val leftSqBrToken by Tokens.leftSqBr
    private val rightSqBrToken by Tokens.rightSqBr
    private val leftBrToken by Tokens.leftBr
    private val rightBrToken by Tokens.rightBr
    private val exclamationMarkToken by Tokens.exclamationMark
    private val ampersandToken by Tokens.ampersand
    private val pipeToken by Tokens.pipe
    private val periodToken by Tokens.period
    private val commaToken by Tokens.comma
    private val colonToken by Tokens.colon
    private val semicolonToken by Tokens.semicolon
    private val questionMarkToken by Tokens.questionMark
    private val backslashToken by Tokens.backslash
    private val dollarToken by Tokens.dollar
    private val hyphenToken by Tokens.hyphen
    private val plusToken by Tokens.plus
    private val asteriskToken by Tokens.asterisk
    private val forwardSlashToken by Tokens.forwardSlash
    private val modToken by Tokens.percent
    private val powToken by Tokens.caret
    private val equalsToken by Tokens.equals
    private val lessThanToken by Tokens.lessThan
    private val greaterThanToken by Tokens.greaterThan

    // Println token.
    private val printlnToken by Tokens.println

    // Skip token.
    private val skipToken by Tokens.skip

    // Declaration.
    private val valToken by Tokens.`val`
    private val varToken by Tokens.`var`

    // If-else tokens.
    private val ifToken by Tokens.`if`
    private val thenToken by Tokens.then
    private val elifToken by Tokens.elif
    private val elseToken by Tokens.`else`
    private val fiToken by Tokens.fi

    // Loop tokens.
    private val forToken by Tokens.`for`
    private val whileToken by Tokens.`while`
    private val doToken by Tokens.`do`
    private val odToken by Tokens.od
    private val inToken by Tokens.`in`

    // Try-catch tokens.
    private val tryToken by Tokens.`try`
    private val catchToken by Tokens.catch
    private val finallyToken by Tokens.finally
    private val yrtToken by Tokens.yrt
    private val throwToken by Tokens.`throw`

    // Function tokens.
    private val funToken by Tokens.`fun`
    private val beginToken by Tokens.begin
    private val endToken by Tokens.end
    private val returnToken by Tokens.`return`

    // Pair.
    private val toToken by Tokens.to

    // Literal tokens.
    private val nullToken by Tokens.`null`
    private val booleanToken by Tokens.boolean
    private val integerToken by Tokens.integer
    private val exponentToken by Tokens.exponent
    private val numberSuffixToken by Tokens.numberSuffix
    private val doubleQuotedStringToken by Tokens.doubleQuotedString
    private val charToken by Tokens.character

    // Id token.
    private val idToken by Tokens.id

    // Other token.
    private val otherToken by regexToken($$"""[^$\\{]+""")

    private val evenDollars by oneOrMore(dollarToken * -dollarToken) use {
        dollarsEscaper(joinToString("", transform = TokenMatch::text).repeat(2))
    }

    private val plainString by doubleQuotedStringToken use { text.substring(1, text.lastIndex) }

    private val key = (idToken or integerToken).use { text } or parser(::plainString)

    // Interpolate.
    private val interpolate =
        -dollarToken * separatedTerms(key, periodToken) map valueGetter

    private val interpolateBraces =
        -dollarToken * -leftBrToken * separatedTerms(key, periodToken) * -rightBrToken map valueGetter

    private val evenBS by oneOrMore(backslashToken * -backslashToken) use {
        joinToString("", transform = TokenMatch::text)
    }

    private val escEvaluate by (optional(evenBS) * backslashToken * leftBrToken) map { (ebs, bs, lbr) ->
        "${evaluateEscaper("${ebs.orEmpty()}${bs.text}")}${lbr.text}"
    }

    private val evaluate =
        (optional(evenBS) * -leftBrToken * -leftBrToken * ProgramGrammar.mapToken { token ->
            when (token.type) {
                wsToken -> token.copy(type = Tokens.wsIgnore)
                nlToken -> token.copy(type = Tokens.nlIgnore)
                else -> token
            }
        } * -optional(wsToken) * -rightBrToken * -rightBrToken)
            .map { (bs, program) ->
                var result = program { name -> getter(listOf(name)) }
                if (bs != null) result = "$bs$result"
                result
            }

    private val text by (wsToken or
            nlToken or
            leftParToken or
            rightParToken or
            leftSqBrToken or
            rightSqBrToken or
            leftBrToken or
            rightBrToken or
            exclamationMarkToken or
            ampersandToken or
            pipeToken or
            periodToken or
            commaToken or
            colonToken or
            semicolonToken or
            questionMarkToken or
            backslashToken or
            dollarToken or
            hyphenToken or
            plusToken or
            asteriskToken or
            forwardSlashToken or
            modToken or
            powToken or
            equalsToken or
            lessThanToken or
            greaterThanToken or
            printlnToken or
            skipToken or
            valToken or
            varToken or
            ifToken or
            thenToken or
            elifToken or
            elseToken or
            fiToken or
            forToken or
            whileToken or
            doToken or
            odToken or
            inToken or
            tryToken or
            catchToken or
            finallyToken or
            yrtToken or
            throwToken or
            funToken or
            beginToken or
            endToken or
            returnToken or
            toToken or
            nullToken or
            booleanToken or
            integerToken or
            exponentToken or
            numberSuffixToken or
            doubleQuotedStringToken or
            charToken or
            idToken or
            otherToken) use { text }

    // Explicitly consume whitespaces because they  are implicitly ignored.
    override val rootParser: Parser<Any?> =
        zeroOrMore(
            listOfNotNull(
                if (SubstituteOption.INTERPOLATE in options || SubstituteOption.INTERPOLATE_BRACES in options)
                    evenDollars else null,
                if (SubstituteOption.INTERPOLATE in options) interpolate else null,
                if (SubstituteOption.INTERPOLATE_BRACES in options) interpolateBraces else null,
                if (SubstituteOption.EVALUATE in options) escEvaluate else null,
                if (SubstituteOption.EVALUATE in options) evaluate else null,
                text
            ).reduce { l, r -> l or r }
        ).map { values -> if (values.size == 1) values.single() else values.joinToString("") }
}

