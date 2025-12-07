package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.collections.takeUnlessEmpty
import klib.data.type.primitives.string.case.toCamelCase
import klib.data.type.primitives.string.tokenization.combinators.asJust
import klib.data.type.primitives.string.tokenization.combinators.leftAssociative
import klib.data.type.primitives.string.tokenization.combinators.map
import klib.data.type.primitives.string.tokenization.combinators.oneOrMore
import klib.data.type.primitives.string.tokenization.combinators.optional
import klib.data.type.primitives.string.tokenization.combinators.or
import klib.data.type.primitives.string.tokenization.combinators.rightAssociative
import klib.data.type.primitives.string.tokenization.combinators.separatedTerms
import klib.data.type.primitives.string.tokenization.combinators.times
import klib.data.type.primitives.string.tokenization.combinators.unaryMinus
import klib.data.type.primitives.string.tokenization.combinators.use
import klib.data.type.primitives.string.tokenization.combinators.zeroOrMore
import klib.data.type.primitives.string.tokenization.grammar.Grammar
import klib.data.type.primitives.string.tokenization.grammar.parseToEnd
import klib.data.type.primitives.string.tokenization.grammar.parser
import klib.data.type.primitives.string.tokenization.lexer.TokenMatch
import klib.data.type.primitives.string.tokenization.lexer.Tokens
import klib.data.type.primitives.string.tokenization.parser.Parser
import klib.data.type.primitives.string.tokenization.parser.Parsers

public fun String.compile(): Program = ProgramGrammar.parseToEnd(this)

@Suppress("UNUSED")
public object ProgramGrammar : Grammar<Program>() {
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
    private val dollar by Tokens.dollar
    private val ampersandToken by Tokens.ampersand
    private val atToken by Tokens.at
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
    private val whenToken by Tokens.`when`
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
    private val breakToken by Tokens.`break`
    private val continueToken by Tokens.`continue`

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

    // Pair to token.
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
    private val id by Parsers.id or (exponentToken or numberSuffixToken).use(TokenMatch::text) or Parsers.doubleQuotedString
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
    private val whenTerm by parser(::whenStatement) map ::StatementExpression
    private val tryTerm by parser(::tryStatement) map ::StatementExpression
    private val throwTerm by parser(::throwStatement) map ::StatementExpression

    private val reference by Parsers.reference or Parsers.reference_braced use ::Reference

    private val baseTerm by literal or
            funCall or
            variable or
            reference or
            parenthesesTerm or
            ifTerm or
            whenTerm or
            tryTerm or
            throwTerm

    private val memberTerm: Parser<(Expression, Boolean) -> Expression> by
    (-periodToken * id * optional(arguments))
        .map { (name, args) ->
            { receiver, optional ->
                when (name) {
                    // Any.
                    Class::class.simpleName!!.toCamelCase() -> Class(receiver, optional)
                    SimpleName::class.simpleName!!.toCamelCase() -> SimpleName(receiver, optional)

                    // Pair.
                    First::class.simpleName!!.toCamelCase() -> First(receiver, optional)
                    Second::class.simpleName!!.toCamelCase() -> Second(receiver, optional)

                    // Collection.
                    ToList::class.simpleName!!.toCamelCase() -> ToList(receiver, optional)
                    ToMutableList::class.simpleName!!.toCamelCase() -> ToMutableList(receiver, optional)
                    ToMap::class.simpleName!!.toCamelCase() -> ToMap(receiver, optional)
                    Iterator::class.simpleName!!.toCamelCase() -> Iterator(receiver, optional)
                    HasNext::class.simpleName!!.toCamelCase() -> HasNext(receiver, optional)
                    Next::class.simpleName!!.toCamelCase() -> Next(receiver, optional)
                    Size::class.simpleName!!.toCamelCase() -> Size(receiver, optional)

                    // List.
                    Add::class.simpleName!!.toCamelCase() -> Add(listOf(receiver) + args!!, optional)
                    AddAll::class.simpleName!!.toCamelCase() -> AddAll(listOf(receiver) + args!!, optional)
                    Remove::class.simpleName!!.toCamelCase() -> Remove(listOf(receiver) + args!!, optional)
                    Clear::class.simpleName!!.toCamelCase() -> Clear(receiver, optional)

                    // Map.
                    Pairs::class.simpleName!!.toCamelCase() -> Pairs(receiver, optional)
                    Keys::class.simpleName!!.toCamelCase() -> Keys(receiver, optional)
                    Values::class.simpleName!!.toCamelCase() -> Values(receiver, optional)
                    ToMutableMap::class.simpleName!!.toCamelCase() -> ToMutableMap(receiver, optional)

                    // Exception.
                    Cause::class.simpleName!!.toCamelCase() -> Cause(receiver, optional)
                    Message::class.simpleName!!.toCamelCase() -> Message(receiver, optional)
                    StackTraceToString::class.simpleName!!.toCamelCase() -> StackTraceToString(receiver, optional)

                    else -> throw IllegalArgumentException("Unknown member call '$name'")
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
                else -> throw IllegalArgumentException("Unexpected postfix operator")
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

            else -> throw IllegalArgumentException("Unknown multiplication token '$o'")
        }
    }

    private val sum by Tokens.plus or Tokens.hyphen
    private val math by leftAssociative(multiplicationOrTerm, sum) { l, o, r ->
        when (o.type) {
            plusToken -> l + r
            hyphenToken -> l - r

            else -> throw IllegalArgumentException("Unknown sum token '$o'")
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

                    else -> throw IllegalArgumentException("Unknown comparison token '$o'")
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

    private val skipStatement by skipToken asJust Skip

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
    (assignee * optional(compound) * -equalsToken * expr).map { (assignee, compound, value) ->
        Assign(
            assignee,
            when (compound?.type) {
                null -> value
                plusToken -> assignee + value
                hyphenToken -> assignee - value
                asteriskToken -> assignee * value
                forwardSlashToken -> assignee / value
                remToken -> assignee % value
                powToken -> assignee pow value
                else -> throw IllegalArgumentException("Unknown compound assign '${compound.type}'")
            }
        )
    }

    // If-else.
    private val ifStatement by
    (-ifToken * expr * -thenToken *
            parser(::statementsChain) *
            zeroOrMore(-elifToken * expr * -thenToken * parser(::statementsChain)) *
            optional(-elseToken * parser(::statementsChain)).map { body -> body ?: Skip } *
            -fiToken
            ).map { (condition, thenBody, elIfs, elseBody) ->
            val elses =
                elIfs.foldRight(elseBody.scoped()) { (condition, thenBody), elseBody ->
                    If(condition, thenBody.scoped(), elseBody)
                }
            If(condition, thenBody.scoped(), elses)
        }

    // When.
    private val whenStatement by
    (-whenToken * optional(declareStatement or expr.use(::ExpressionStatement)) * -beginToken *
            zeroOrMore(expr * -thenToken * parser(::statementsChain) * -fiToken) *
            optional(-elseToken * parser(::statementsChain)).map { body -> body ?: Skip } *
            -endToken
            ).map { (init, branches, elseBody) ->
            val elses =
                branches.foldRight(elseBody.scoped()) { (condition, thenBody), elseBody ->
                    If(condition, thenBody.scoped(), elseBody)
                }
            Chain(init ?: Skip, elses).scoped()
        }

    // Loop.
    private val label by -atToken * id
    private val forStatement by
    (optional(label) * -forToken * parser(::statement) * -commaToken * expr * -commaToken * parser(::statement) * -doToken *
            parser(::statementsChain) * -odToken).map { (label, init, condition, step, body) ->
        Chain(init, While(label, condition, Chain(body, step).scoped())).scoped()
    }

    private val foreachStatement by
    (optional(label) * -forToken * variable * -inToken * expr * -doToken * parser(::statementsChain) * -odToken)
        .map { (label, item, receiver, body) ->
            val iterator = Variable("__iter_${item.name}", Type.ITERATOR)
            val declareIterator = Declare(iterator, Iterator(receiver, false), false)
            val condition = HasNext(iterator, false)
            val declareItem = Declare(item, Next(iterator, false), false)

            Chain(declareIterator, While(label, condition, Chain(declareItem, body).scoped())).scoped()
        }

    private val whileStatement by (optional(label) * -whileToken * expr * -doToken * parser(::statementsChain) * -odToken)
        .map { (label, condition, body) -> While(label, condition, body.scoped()) }

    private val doStatement by
    (optional(label) * -doToken * parser(::statementsChain) * -odToken * -whileToken * expr)
        .map { (label, body, condition) ->
            Chain(body.scoped(), While(label, condition, body.scoped()))
        }

    private val breakStatement by -breakToken * optional(id) map ::Break
    private val continueStatement by -continueToken * optional(id) map ::Continue

    // Exception.
    private val tryStatement by
    (-tryToken * parser(::statementsChain) *
            zeroOrMore(-catchToken * -leftParToken * variable * -rightParToken * parser(::statementsChain)).use {
                map { (variable, body) -> Try.Catch(variable, body.scoped()) }
            } *
            optional(-finallyToken * parser(::statementsChain)) *
            -yrtToken
            ).map { (body, catches, finallyBody) -> Try(body.scoped(), catches, finallyBody?.scoped() ?: Skip) }

    private val throwStatement by
    -throwToken * idToken * -leftParToken * optional(expr) * -rightParToken map { (type, message) ->
        Throw(type.text.toType(), message ?: StringLiteral(""))
    }

    private val returnStatement by -returnToken * optional(expr) map { value -> Return(value ?: UnitLiteral) }

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
        .map { (name, parameters, body) -> Function(name, parameters, body.scoped()) }

    override val rootParser: Parser<Program> by
    (oneOrMore(function or (statement * optional(semicolonToken) use { t1 })) *
            -zeroOrMore(wsIgnoreToken or nlIgnoreToken or hashCommentIgnoreToken))
        .map { program ->
            Program(
                Function(
                    "main",
                    listOf(),
                    program.filterIsInstance<Statement>().takeUnlessEmpty()
                        ?.let { statements -> chainOf(*statements.toTypedArray()).scoped() }
                        ?: Skip
                ),
                program.filterIsInstance<Function>()
            )
        }
}
