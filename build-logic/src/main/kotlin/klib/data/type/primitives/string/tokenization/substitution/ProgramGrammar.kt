package klib.data.type.primitives.string.tokenization.substitution

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import com.github.h0tk3y.betterParse.utils.Tuple2
import klib.data.type.primitives.string.tokenization.Parsers
import klib.data.type.primitives.string.tokenization.Tokens
import klib.data.type.primitives.string.tokenization.mapToken
import klib.data.type.primitives.string.tokenization.mapWithMatches
import klib.data.type.primitives.string.case.toCamelCase
import kotlin.collections.Set
import kotlin.collections.map

public fun String.compile(): Program = ProgramGrammar.parseToEnd(this)

@Suppress("UNUSED")
internal object ProgramGrammar : Grammar<Program>() {
    // Comment tokens.
    private val hashCommentIgnoreToken by Tokens.hashCommentIgnore

    // Whitespace and newline tokens.
    private val wsIgnoreToken by Tokens.wsIgnore
    private val nlIgnoreToken by Tokens.nlIgnore

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
    private val hyphenToken by Tokens.hyphen
    private val plusToken by Tokens.plus
    private val asteriskToken by Tokens.asterisk
    private val forwardSlashToken by Tokens.forwardSlash
    private val remToken by Tokens.percent
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
    private val characterToken by Tokens.character

    // Id token.
    private val idToken by Tokens.id


    // Literals.
    private val literal by (Parsers.`null` or
            Parsers.boolean or
            Parsers.number or
            Parsers.character) map ::Literal or
            Parsers.doubleQuotedString.map(::StringLiteral)

    // Variable.
    private val id by (Tokens.id or exponentToken or numberSuffixToken use { text } or Parsers.doubleQuotedString)
    private val type by (idToken * optional(questionMarkToken)) map { (type, optional) ->
        "${type.text}${optional?.text.orEmpty()}".toType()
    }
    private val variable by (id * optional(-colonToken * type))
        .map { (name, type) -> Variable(name, type ?: Type.UNDEFINED) }

    private val arguments by -leftParToken * separatedTerms(
        parser(::expr),
        commaToken,
        acceptZero = true
    ) * -rightParToken

    private val funCall by
    (id * arguments)
        .map { (name, arguments) ->
            name.toTypeOrNull()?.let { type -> TypeCall(type, arguments) }
                ?: FunctionCall(name, arguments)
        }

    private val parenthesesTerm by -leftParToken * parser(::expr) * -rightParToken

    private val ifTerm by parser(::ifStatement) map ::StatementExpression
    private val tryTerm by parser(::tryStatement) map ::StatementExpression
    private val throwTerm by parser(::throwStatement) map ::StatementExpression

    private val baseTerm by literal or funCall or variable or parenthesesTerm or ifTerm or tryTerm or throwTerm

    private val memberTerm: Parser<(Expression, Boolean) -> Expression> by
    (-periodToken * id * optional(arguments))
        .map { (name, args) ->
            { receiver, optional ->
                when (name) {
                    // Pair.
                    First::class.simpleName!!.toCamelCase() -> First(listOf(receiver), optional)
                    Second::class.simpleName!!.toCamelCase() -> Second(listOf(receiver), optional)

                    // Collection.
                    ToList::class.simpleName!!.toCamelCase() -> ToList(listOf(receiver), optional)
                    ToMutableList::class.simpleName!!.toCamelCase() -> ToMutableList(listOf(receiver), optional)
                    ToMap::class.simpleName!!.toCamelCase() -> ToMap(listOf(receiver), optional)

                    // List.
                    Add::class.simpleName!!.toCamelCase() -> Add(listOf(receiver) + args!!, optional)
                    AddAll::class.simpleName!!.toCamelCase() -> AddAll(listOf(receiver) + args!!, optional)
                    Remove::class.simpleName!!.toCamelCase() -> Remove(listOf(receiver) + args!!, optional)
                    Clear::class.simpleName!!.toCamelCase() -> Clear(listOf(receiver), optional)
                    Size::class.simpleName!!.toCamelCase() -> Size(listOf(receiver), optional)

                    // Map.
                    Pairs::class.simpleName!!.toCamelCase() -> Pairs(listOf(receiver), optional)
                    Keys::class.simpleName!!.toCamelCase() -> Keys(listOf(receiver), optional)
                    Values::class.simpleName!!.toCamelCase() -> Values(listOf(receiver), optional)
                    ToMutableMap::class.simpleName!!.toCamelCase() -> ToMutableMap(listOf(receiver), optional)

                    // Exception.
                    Cause::class.simpleName!!.toCamelCase() -> Cause(listOf(receiver), optional)
                    Message::class.simpleName!!.toCamelCase() -> Message(listOf(receiver), optional)
                    StackTraceToString::class.simpleName!!.toCamelCase() -> StackTraceToString(
                        listOf(receiver),
                        optional
                    )

                    else -> error("Unknown member call '$name'")
                }
            }
        }

    private val keyTerm: Parser<(Expression, Boolean) -> Expression> =
        (-leftSqBrToken * parser(::expr) * -rightSqBrToken)
            .map { key -> { receiver, optional -> Get(listOf(receiver, key), optional) } }

    private val term by (baseTerm * zeroOrMore(optional(questionMarkToken) * (memberTerm or keyTerm)))
        .map { (base, suffixes) -> suffixes.fold(base) { acc, (optional, suffix) -> suffix(acc, optional != null) } }

    // Postfix.
    private val notNull by (exclamationMarkToken * -exclamationMarkToken)
    private val inc by plusToken * -plusToken
    private val dec by hyphenToken * -hyphenToken
    private val postfixTerm by
    (term * zeroOrMore(notNull or inc or dec)).map { (base, ops) ->
        ops.fold(base) { acc, op ->
            when (op.type) {
                exclamationMarkToken -> NotNull(acc)
                plusToken -> Inc(acc)
                hyphenToken -> Dec(acc)
                else -> error("Unexpected postfix operator")
            }
        }
    }

    // Math.
    private val pow by rightAssociative(postfixTerm, powToken) { l, _, r -> l pow r }
    private val prefixTerm: Parser<Expression> by
    (-plusToken * -plusToken * parser(::prefixTerm)).map(Expression::preInc) or
            (-hyphenToken * -hyphenToken * parser(::prefixTerm)).map(Expression::preDec) or
            (-plusToken * parser(::prefixTerm)).map { value -> value } or
            (-hyphenToken * parser(::prefixTerm)).map(Expression::unaryMinus) or
            (-exclamationMarkToken * parser(::prefixTerm)).map(Expression::not) or
            pow
    private val multiplication by Tokens.asterisk or Tokens.forwardSlash or Tokens.percent
    private val multiplicationOrTerm by leftAssociative(prefixTerm, multiplication) { l, o, r ->
        when (o.type) {
            asteriskToken -> l * r
            forwardSlashToken -> l / r
            remToken -> l % r

            else -> error("Unsupported multiplication token '$o'")
        }
    }

    private val sum by Tokens.plus or Tokens.hyphen
    private val math by leftAssociative(multiplicationOrTerm, sum) { l, o, r ->
        when (o.type) {
            plusToken -> l + r
            hyphenToken -> l - r

            else -> error("Unsupported sum token '$o'")
        }
    }

    // Comparison.
    private val refEquals = Tokens.equals * Tokens.equals * Tokens.equals use { 0 }
    private val equals = Tokens.equals * Tokens.equals use { 1 }
    private val refNotEquals by Tokens.exclamationMark * Tokens.equals * Tokens.equals use { 2 }
    private val notEquals by Tokens.exclamationMark * Tokens.equals use { 3 }
    private val lessOrEqualsThan = Tokens.lessThan * Tokens.equals use { 4 }
    private val greaterOrEqualsThan = Tokens.greaterThan * Tokens.equals use { 6 }
    private val comparison = refEquals or
            equals or
            refNotEquals or
            notEquals or
            lessOrEqualsThan or
            lessThanToken.use { 5 } or
            greaterOrEqualsThan or
            greaterThanToken.use { 7 }

    private val comparisonOrMath: Parser<Expression> by (math * optional(comparison * math))
        .map { (l, tail) ->
            tail?.let { (o, r) ->
                when (o) {
                    0 -> l refEq r
                    1 -> l eq r
                    2 -> l refNeq r
                    3 -> l neq r
                    4 -> l leq r
                    5 -> l lt r
                    6 -> l geq r
                    7 -> l gt r

                    else -> error("Unsupported comparison token '$o'")
                }
            } ?: l
        }

    // Logic.
    private val and by ampersandToken * -ampersandToken
    private val andChain by leftAssociative(comparisonOrMath, and) { l, _, r -> l and r }
    private val or by pipeToken * -pipeToken
    private val orChain by leftAssociative(andChain, or) { l, _, r -> l or r }

    // Coalesce
    private val coalesce by Tokens.questionMark * Tokens.colon
    private val coalesceChain by rightAssociative(orChain, coalesce) { l, _, r -> l coalesce r }

    // Pair.
    private val pair: Parser<Expression> =
        (coalesceChain * -toToken * coalesceChain)
            .map { (l, r) -> Pair(l, r) }

    private val expr by pair or coalesceChain

    private val printlnStatement by -printlnToken * -leftParToken * expr * -rightParToken map ::Println

    private val skipStatement by skipToken.map { Skip }

    private val declareStatement by ((valToken asJust false) or (varToken asJust true)) *
            variable * optional(-equalsToken * expr) map { (mutable, variable, value) ->
        Declare(variable, value, mutable)
    }

    // Assign.
    private val assignee by
    (variable * zeroOrMore(optional(questionMarkToken) * (memberTerm or keyTerm)))
        .map { (base, path) ->
            path.fold(base as Expression) { acc, (optional, key) -> key(acc, optional != null) }
        }

    private val compound by plusToken or hyphenToken or asteriskToken or forwardSlashToken or remToken or powToken

    private val assignStatement: Parser<Statement> by
    (assignee * optional(compound) * -equalsToken * expr).map { (receiver, compound, value) ->
        Assign(
            receiver,
            when (compound?.type) {
                null -> value
                plusToken -> receiver + value
                hyphenToken -> receiver - value
                asteriskToken -> receiver * value
                forwardSlashToken -> receiver / value
                remToken -> receiver % value
                powToken -> receiver pow value
                else -> error("Unsupported compound assign '${compound.type}'")
            }
        )
    }

    // If-else.
    private val ifStatement: Parser<If> by
    (-ifToken * expr * -thenToken *
            parser(::statementsChain) *
            zeroOrMore(-elifToken * expr * -thenToken * parser(::statementsChain)) *
            optional(-elseToken * parser(::statementsChain)).map { body -> body ?: Skip } *
            -fiToken
            ).map { (condition, thenBody, elIfs, elseBody) ->
            val elses = elIfs.foldRight(elseBody) { (elifC, elifB), el -> If(elifC, elifB, el) }
            If(condition, thenBody, elses)
        }

    // Loop.
    private val forStatement by
    (-forToken * parser(::statement) * -commaToken * expr * -commaToken * parser(::statement) * -doToken *
            parser(::statementsChain) * -odToken).map { (init, condition, step, body) ->
        Chain(init, While(condition, Chain(body, step).scoped()))
    }

    private val foreachStatement by
    (-forToken * variable * -inToken * expr * -doToken * parser(::statementsChain) * -odToken)
        .map { (element, receiver, body) -> Foreach(element, receiver, body.scoped()) }

    private val whileStatement by (-whileToken * expr * -doToken * parser(::statementsChain) * -odToken)
        .map { (condition, body) -> While(condition, body.scoped()) }

    private val doStatement by
    (-doToken * parser(::statementsChain) * -odToken * -whileToken * expr).map { (body, condition) ->
        Chain(body.scoped(), While(condition, body.scoped()))
    }

    // Exception.
    private val tryStatement: Parser<Try> by
    (-tryToken * parser(::statementsChain) *
            zeroOrMore(-catchToken * -leftParToken * variable * -rightParToken * parser(::statementsChain)).use {
                map { (variable, body) ->
                    Try.Catch(variable, body)
                }
            } *
            optional(-finallyToken * parser { statementsChain }) *
            -yrtToken
            ).map { (body, catch, finally) ->
            Try(body, catch, finally ?: Skip)
        }

    private val throwStatement: Parser<Throw> by
    -throwToken * idToken * -leftParToken * optional(expr) * -rightParToken map { (type, message) ->
        Throw(type.text.toType(), message ?: StringLiteral(""))
    }

    private val returnStatement by -returnToken * optional(expr) map { value ->
        Return(value ?: UnitLiteral)
    }

    private val expressionStatement by expr map ::ExpressionStatement

    private val statement: Parser<Statement> by printlnStatement or
            skipStatement or
            declareStatement or
            assignStatement or
            ifStatement or
            forStatement or
            foreachStatement or
            whileStatement or
            doStatement or
            tryStatement or
            throwStatement or
            returnStatement or
            expressionStatement

    private val statementsChain by
    separatedTerms(statement, optional(semicolonToken)) * -optional(semicolonToken) use { chainOf(*toTypedArray()) }

    private val function by (-funToken * id * -leftParToken *
            separatedTerms(
                variable,
                commaToken,
                acceptZero = true
            )
            * -rightParToken * -beginToken * statementsChain * -endToken)
        .map { (name, parameters, body) -> Function(name, parameters, body) }

    override val rootParser: Parser<Program> by
    (oneOrMore(function or (statement * optional(semicolonToken) use { t1 })) *
            -zeroOrMore(wsIgnoreToken or nlIgnoreToken or hashCommentIgnoreToken))
        .map { program ->
            Program(
                Function(
                    "main",
                    listOf(),
                    chainOf(*program.filterIsInstance<Statement>().ifEmpty {
                        listOf(Skip)
                    }.toTypedArray())
                ),
                program.filterIsInstance<Function>()
            )
        }
}
