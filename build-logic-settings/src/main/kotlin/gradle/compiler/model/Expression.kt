package gradle.compiler.model

public sealed class Expression

public data class Const(val value: Int) : Expression()

public data class Number(val value: String) : Expression()
public data class Variable(val name: String) : Expression()

public data class Reference(val id: String) : Expression()

public data class StringLiteral(val value: String) : Expression()

public data class Array(
    val type: String,
    val elementTypes: List<String>?,
    val values: List<Expression>
) : Expression()

public data class FunctionCall(
    val functionDeclaration: FunctionDeclaration,
    val argumentExpressions: List<Expression>
) : Expression() {

    init {
        require(argumentExpressions.size == functionDeclaration.parameters.size)
    }
}

public data class UnaryOperation(val operand: Expression, val kind: UnaryOperationKind) : Expression()
public sealed class UnaryOperationKind
public object Not : UnaryOperationKind()

public fun UnaryOperationKind.semantics(x: Int): Int = when (this) {
    Not -> (x == 0).asBit()
}

public data class BinaryOperation(val left: Expression, val right: Expression, val kind: BinaryOperationKind) : Expression()
public sealed class BinaryOperationKind
public object Plus : BinaryOperationKind()
public object Minus : BinaryOperationKind()
public object Times : BinaryOperationKind()
public object Div : BinaryOperationKind()
public object Rem : BinaryOperationKind()
public object And : BinaryOperationKind()
public object Or : BinaryOperationKind()
public object Eq : BinaryOperationKind()
public object Neq : BinaryOperationKind()
public object Gt : BinaryOperationKind()
public object Lt : BinaryOperationKind()
public object Leq : BinaryOperationKind()
public object Geq : BinaryOperationKind()

public fun Boolean.asBit(): Int = if (this) 1 else 0

public fun BinaryOperationKind.semantics(l: Int, r: Int): Int = when (this) {
    Plus -> l + r
    Minus -> l - r
    Times -> l * r
    Div -> l / r
    Rem -> l % r
    And -> (l != 0 && r != 0).asBit()
    Or -> (l != 0 || r != 0).asBit()
    Eq -> (l == r).asBit()
    Neq -> (l != r).asBit()
    Gt -> (l > r).asBit()
    Lt -> (l < r).asBit()
    Leq -> (l <= r).asBit()
    Geq -> (l >= r).asBit()
}
