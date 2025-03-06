package gradle.compiler

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.asJust
import com.github.h0tk3y.betterParse.combinators.leftAssociative
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import gradle.compiler.model.And
import gradle.compiler.model.Array
import gradle.compiler.model.Assign
import gradle.compiler.model.BinaryOperation
import gradle.compiler.model.CatchBranch
import gradle.compiler.model.Chain
import gradle.compiler.model.Const
import gradle.compiler.model.Div
import gradle.compiler.model.Eq
import gradle.compiler.model.ExceptionType
import gradle.compiler.model.Expression
import gradle.compiler.model.FunctionCall
import gradle.compiler.model.FunctionCallStatement
import gradle.compiler.model.FunctionDeclaration
import gradle.compiler.model.Geq
import gradle.compiler.model.Gt
import gradle.compiler.model.If
import gradle.compiler.model.Intrinsic
import gradle.compiler.model.Leq
import gradle.compiler.model.Lt
import gradle.compiler.model.Minus
import gradle.compiler.model.Neq
import gradle.compiler.model.Not
import gradle.compiler.model.Number
import gradle.compiler.model.Or
import gradle.compiler.model.Plus
import gradle.compiler.model.Program
import gradle.compiler.model.Reference
import gradle.compiler.model.Rem
import gradle.compiler.model.Return
import gradle.compiler.model.Skip
import gradle.compiler.model.Statement
import gradle.compiler.model.StatementBlock
import gradle.compiler.model.StringLiteral
import gradle.compiler.model.Throw
import gradle.compiler.model.Times
import gradle.compiler.model.Try
import gradle.compiler.model.UnaryOperation
import gradle.compiler.model.UnresolvedFunction
import gradle.compiler.model.Variable
import gradle.compiler.model.While
import gradle.compiler.model.chainOf

internal object ProgramGrammar : Grammar<Program>() {

    private val LPAR by literalToken("(")
    private val RPAR by literalToken(")")

    private val LSQ by literalToken("[")
    private val RSQ by literalToken("]")

    private val LBRC by literalToken("{")
    private val RBRC by literalToken("}")

    private val DOLLAR by literalToken("$")

    private val ARRAY_LIKE_OF by regexToken("arrayOf|listOf|mutableListOf|setOf|mutableSetOf")
    private val MAP_OF by regexToken("mapOf|mutableMapOf")

    private val PLUS by literalToken("+")
    private val MINUS by literalToken("-")
    private val DIV by literalToken("/")
    private val MOD by literalToken("%")
    private val TIMES by literalToken("*")
    private val OR by literalToken("||")
    private val AND by literalToken("&&")
    private val EQU by literalToken("==")
    private val NEQ by literalToken("!=")
    private val LEQ by literalToken("<=")
    private val GEQ by literalToken(">=")
    private val LT by literalToken("<")
    private val GT by literalToken(">")

    private val NOT by literalToken("!")

    private val COMMA by literalToken(",")

    private val COLON by literalToken(":")
    private val SEMI_COLON by literalToken(";")

    private val ASIGN by literalToken("=")

    private val TO by literalToken("to")

    private val IF by regexToken("if\\b")

    private val ELSE by regexToken("else\\b")

    private val WHEN by regexToken("when\\b")

    private val IMPL by literalToken("->")

    private val WHILE by regexToken("while\\b")
    private val FOR by regexToken("for\\b")

    private val TRY by regexToken("try\\b")
    private val CATCH by regexToken("catch\\b")
    private val FINALLY by regexToken("finally\\b")
    private val THROW by regexToken("throw\\b")

    private val SKIP by regexToken("skip\\b")

    private val REPEAT by regexToken("repeat\\b")
    private val UNTIL by regexToken("until\\b")

    private val FUN by regexToken("fun\\b")
    private val RETURN by regexToken("return\\b")

    private val TRUE by regexToken("true\\b")
    private val FALSE by regexToken("false\\b")

    private val INTEGER by regexToken("\\d+")
    private val DOT by literalToken(".")
    private val EXPONENT by regexToken("[eE]")

    private val CHAR by regexToken("'.'")
    private val STRING by regexToken(""""([^"\\]*(?:\\{2})*|(?:\\")?)*"""")

    private val ID by regexToken("[A-Za-z]\\w*")
    private val REF_ID by regexToken("[\\w.]+")

    private val NON_ESCAPED by regexToken("""(?:\\{2})*""")
    private val WS by regexToken("\\s+", ignore = true)
    private val NEWLINE by regexToken("[\r\n]+", ignore = true)

    private val signToKind = mapOf(
        OR to Or,
        AND to And,
        LT to Lt,
        GT to Gt,
        EQU to Eq,
        NEQ to Neq,
        LEQ to Leq,
        GEQ to Geq,
        PLUS to Plus,
        MINUS to Minus,
        TIMES to Times,
        DIV to Div,
        MOD to Rem,
    )

    private val EXPONENT_PART = -EXPONENT and INTEGER
    private val FLOATING_POINT_PART = -DOT and optional(INTEGER)
    private val ONLY_FLOATING_PART = -DOT and INTEGER
    private val POSITIVE_NUMBER: Parser<String> = ((INTEGER and optional(FLOATING_POINT_PART))
        .map { (int, floatPart) ->
            int.text + floatPart?.let { ".${it.text}" }.orEmpty()
        } or
        (ONLY_FLOATING_PART map { ".${it.text}" }) and optional(EXPONENT_PART map { "e${it.text}" }))
        .map { (p1, p2) ->
            p1 + p2.orEmpty()
        }

    private val NUMBER: Parser<Number> = (optional(MINUS) and POSITIVE_NUMBER)
        .map { (m, num) -> Number((m?.text ?: "") + num) }

    private val const by
    NUMBER or
        CHAR.map { Const(it.text[1].code) } or
        (TRUE asJust Const(1)) or
        (FALSE asJust Const(0))

    private val arrayLike by ARRAY_LIKE_OF *
        (optional(-LT * ID * -GT).map { it?.text?.let(::listOf) } or
            optional(-LT * ID * -COMMA * ID * -GT).map { it?.let { (t1, t2) -> listOf(t1.text, t2.text) } }) *
        -LPAR * separatedTerms(parser(::expr), COMMA, acceptZero = true) * -RPAR map { (type, elemTypes, values) ->
        Array(type.text, elemTypes, values)
    }

    private val pair by parser(::expr) * -WS * -TO * -WS * parser(::expr)

    private val funCall: Parser<FunctionCall> by
    (ID * -LPAR * separatedTerms(parser(::expr), COMMA, acceptZero = true) * -RPAR).map { (name, args) ->
        FunctionCall(UnresolvedFunction(name.text, args.size), args)
    }

    private val variable by ID use { Variable(text) }

    private val reference by -NON_ESCAPED * -DOLLAR * REF_ID use { Reference(text) }

    private val stringLiteral by STRING use { StringLiteral(text) }

    private val notTerm by (-NOT * parser(this::term)) map { UnaryOperation(it, Not) }
    private val parenTerm by -LPAR * parser(this::expr) * -RPAR

    private val nonIndexedTerm: Parser<Expression> by
    reference or
        const or
        funCall or
        notTerm or
        variable or
        parenTerm or
        stringLiteral or
        arrayLike

    private val indexedTerm: Parser<FunctionCall> by
    (nonIndexedTerm * oneOrMore(-LSQ * parser(::expr) * -RSQ))
        .map { (term, indices) ->
            indices.fold(term) { acc, it -> FunctionCall(Intrinsic.ARRGET, listOf(acc, it)) } as FunctionCall
        }

    private val term by indexedTerm or nonIndexedTerm

    private val multiplicationOperator by TIMES or DIV or MOD
    private val multiplicationOrTerm by leftAssociative(term, multiplicationOperator) { l, o, r ->
        BinaryOperation(l, r, signToKind[o.type]!!)
    }

    private val sumOperator by PLUS or MINUS
    private val math: Parser<Expression> = leftAssociative(multiplicationOrTerm, sumOperator) { l, o, r ->
        BinaryOperation(l, r, signToKind[o.type]!!)
    }

    private val comparisonOperator by EQU or NEQ or LT or GT or LEQ or GEQ
    private val comparisonOrMath: Parser<Expression> by (math * optional(comparisonOperator * math))
        .map { (left, tail) -> tail?.let { (op, r) -> BinaryOperation(left, r, signToKind[op.type]!!) } ?: left }

    private val andChain by leftAssociative(comparisonOrMath, AND) { l, _, r -> BinaryOperation(l, r, And) }
    private val orChain by leftAssociative(andChain, OR) { l, _, r -> BinaryOperation(l, r, Or) }
    private val expr: Parser<Expression> by orChain

    private val skipStatement: Parser<Skip> by SKIP.map { Skip }

    private val functionCallStatement: Parser<FunctionCallStatement> by funCall.map(::FunctionCallStatement)

    private val assignmentStatement: Parser<Assign> by
    (variable * -ASIGN * expr).map { (v, e) -> Assign(v, e) }

    private val arrayPutStatement: Parser<FunctionCallStatement> by
    (indexedTerm * -ASIGN * expr)
        .map { (call, value) ->
            FunctionCallStatement(FunctionCall(Intrinsic.ARRSET, call.argumentExpressions + listOf(value)))
        }

    private val ifStatement: Parser<If> by
    (-IF * -LPAR * expr * -RPAR * (parser(::statement) or parser(::statementBlock)) *
        optional(-ELSE * (parser(::statement) or parser(::statementBlock))).map { it ?: Skip }
        ).map { (condExpr, thenBody, elseBody) ->
            If(condExpr, thenBody, elseBody)
        }

    private val tryStatement: Parser<Try> by
    (-TRY * parser(::statementBlock) *
        zeroOrMore(-CATCH * -LPAR * ID * -COLON * ID * -RPAR * parser(::statementBlock)).use {
            map { (exData, exType, block) ->
                CatchBranch(ExceptionType(exType.text), Variable(exData.text), block)
            }
        } *
        optional(-FINALLY * parser(::statementBlock))
        ).map { (body, catch, finally) ->
            Try(body, catch, finally ?: Skip)
        }

    private val throwStatement: Parser<Throw> by
    -THROW * ID * -LPAR * parser(::expr) * -RPAR map { (exType, dataExpr) -> Throw(ExceptionType(exType.text), dataExpr) }

    private val forStatement: Parser<Chain> by
    (-FOR * -LPAR * parser(::statement) * -COMMA * parser(::expr) * -COMMA * parser(::statement) *
        parser(::statementBlock)
        ).map { (init, condition, doAfter, body) ->
            Chain(init, While(condition, Chain(body, doAfter)))
        }

    private val whileStatement: Parser<While> by (-WHILE * expr * -LBRC * parser(::statementsChain) * -RBRC)
        .map { (cond, body) -> While(cond, body) }

    private val repeatStatement: Parser<Chain> by (-REPEAT * parser(::statementsChain) * -UNTIL * expr).map { (body, cond) ->
        Chain(body, While(UnaryOperation(cond, Not), body))
    }

    private val returnStatement: Parser<Return> by -RETURN * expr map (::Return)

    private val statement: Parser<Statement> by skipStatement or
        functionCallStatement or
        assignmentStatement or
        ifStatement or
        whileStatement or
        forStatement or
        repeatStatement or
        returnStatement or
        tryStatement or
        throwStatement or
        arrayPutStatement

    private val functionDeclaration: Parser<FunctionDeclaration> by
    (-FUN * ID * -LPAR * separatedTerms(ID, COMMA, acceptZero = true) * -RPAR * -LBRC * parser(::statementsChain) * -RBRC)
        .map { (name, paramNames, body) ->
            FunctionDeclaration(name.text, paramNames.map { Variable(it.text) }, body)
        }

    private val statementsChain: Parser<Statement> by
    separatedTerms(statement, optional(SEMI_COLON)) * -optional(SEMI_COLON) map { chainOf(*it.toTypedArray()) }

    private val statementBlock by -LBRC * parser(::statementsChain).map(::StatementBlock) * -RBRC

    override val rootParser: Parser<Program> by oneOrMore(functionDeclaration or (statement and optional(SEMI_COLON) use { t1 })).map {
        val functions = it.filterIsInstance<FunctionDeclaration>()
        val statements = it.filterIsInstance<Statement>().ifEmpty { listOf(Skip) }
        val rootFunc = FunctionDeclaration("main", listOf(), chainOf(*statements.toTypedArray()))
        Program(functions + rootFunc, rootFunc)
    }
}

//internal fun readProgram(text: String): Program {
//    val parsed = ProgramGrammar.parseToEnd(text)
////    return resolveCalls(parsed)
//}
