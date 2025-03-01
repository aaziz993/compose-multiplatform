package gradle.compiler.model

public operator fun Expression.plus(other: Expression): BinaryOperation = BinaryOperation(this, other, Plus)

public operator fun Expression.minus(other: Expression): BinaryOperation = BinaryOperation(this, other, Minus)

public operator fun Expression.times(other: Expression): BinaryOperation = BinaryOperation(this, other, Times)

public operator fun Expression.div(other: Expression): BinaryOperation = BinaryOperation(this, other, Div)

public operator fun Expression.rem(other: Expression): BinaryOperation = BinaryOperation(this, other, Rem)

public infix fun Expression.and(other: Expression): BinaryOperation = BinaryOperation(this, other, And)

public infix fun Expression.or(other: Expression): BinaryOperation = BinaryOperation(this, other, Or)

public infix fun Expression.eq(other: Expression): BinaryOperation = BinaryOperation(this, other, Eq)

public infix fun Expression.neq(other: Expression): BinaryOperation = BinaryOperation(this, other, Neq)

public infix fun Expression.lessThan(other: Expression): BinaryOperation = BinaryOperation(this, other, Lt)

public infix fun Expression.greaterThan(other: Expression): BinaryOperation = BinaryOperation(this, other, Gt)

public val Read: FunctionCall = FunctionCall(Intrinsic.READ, emptyList())

@Suppress("FunctionName")
public fun Write(e: Expression): FunctionCallStatement =
    FunctionCallStatement(FunctionCall(Intrinsic.WRITE, listOf(e)))
