package gradle.compiler.model

public sealed class Statement

public object Skip : Statement()
public data class Assign(val variable: Variable, val expression: Expression) : Statement()

public data class If(
    val condition: Expression,
    val trueBranch: Statement,
    val falseBranch: Statement
) : Statement()

public data class While(
    val condition: Expression,
    val body: Statement
) : Statement()

public data class Chain(
    val leftPart: Statement,
    val rightPart: Statement
) : Statement()

public data class Return(
    val expression: Expression
) : Statement()

public data class FunctionCallStatement(
    val functionCall: FunctionCall
) : Statement()

public data class StatementBlock(
    val block: Statement
) : Statement()

public fun chainOf(vararg statements: Statement): Statement = statements.reduce(::Chain)

// Exceptions

public data class ExceptionType(val name: String)

public data class CatchBranch(val exceptionType: ExceptionType, val dataVariable: Variable, val body: Statement)

public data class Try(
    val body: Statement,
    val catchBranches: List<CatchBranch>,
    val finallyStatement: Statement = Skip
) : Statement()

public data class Throw(val exceptionType: ExceptionType, val dataExpression: Expression) : Statement()
