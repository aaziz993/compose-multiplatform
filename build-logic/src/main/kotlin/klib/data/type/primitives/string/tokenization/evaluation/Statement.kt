package klib.data.type.primitives.string.tokenization.evaluation

import klib.data.type.collections.map.pairs
import kotlin.collections.iterator

public sealed class Statement {
    public open fun join(machine: MachineState): MachineState = machine
}

public data class Scoped(val statement: Statement) : Statement() {
    override fun join(machine: MachineState): MachineState = statement.join(machine.pushScope()).popScope()
}

public data class ExpressionStatement(val expression: Expression) : Statement() {
    override fun join(machine: MachineState): MachineState = expression(machine)
}

// Log.
public data class Println(val message: Expression) : Statement() {
    override fun join(machine: MachineState): MachineState = message(machine).run {
        if (shouldReturn) this else copy(log = log + result.toString(), result = Unit)
    }
}

// Skip.
public object Skip : Statement()

public data class Declare(
    val variable: Variable,
    val value: Expression?,
    val mutable: Boolean
) : Statement() {
    override fun join(machine: MachineState): MachineState {
        if (value == null) {
            val boundType = if (variable.type == Type.UNDEFINED) Type.ANY_Q else variable.type
            return machine.declare(variable.name, value, boundType, mutable)
        }

        val after = value(machine)
        if (after.shouldReturn) return after

        val boundType = variable.type.takeUnless { type -> type == Type.UNDEFINED }
            ?: after.result?.let { it::class.simpleName!!.toType() }
            ?: Type.ANY_Q

        return after.declare(variable.name, after.result, boundType, mutable)
    }
}

public data class Assign(val receiver: Expression, val value: Expression) : Statement() {
    override fun join(machine: MachineState): MachineState = when (receiver) {
        is Variable -> value(machine).run {
            if (shouldReturn) this else machine.set(receiver.name, result)
        }

        is Get -> ExpressionStatement(Set(receiver.arguments + value, receiver.optional)).join(machine)

        else -> error("Invalid assignment target")
    }
}

public data class DeclareFunction(val function: Function) : Statement() {
    override fun join(machine: MachineState): MachineState = machine.declareFunction(function)
}

// If-else.
public data class If(
    val condition: Expression,
    val thenBody: Statement,
    val elseBody: Statement
) : Statement() {
    override fun join(machine: MachineState): MachineState = condition(machine).run {
        when {
            shouldReturn -> this
            result as Boolean -> thenBody.join(this)
            else -> elseBody.join(this)
        }
    }
}

public data class While(
    val condition: Expression,
    val body: Statement
) : Statement() {
    override fun join(machine: MachineState): MachineState = condition(machine).run {
        if (shouldReturn || !(result as Boolean)) copy(result = Unit) else join(body.join(this))
    }
}

public data class Foreach(
    val element: Variable,
    val receiver: Expression,
    val body: Statement
) : Statement() {
    override fun join(machine: MachineState): MachineState {
        var state = receiver(machine)
        if (state.shouldReturn) return state

        val iterator = state.result?.iterator() ?: throw NullPointerException("Cannot iterate")

        for (item in iterator) {
            val boundType = element.type.takeUnless { type -> type == Type.UNDEFINED }
                ?: item?.let { it::class.simpleName!!.toType() }
                ?: Type.ANY_Q

            val scoped = state.pushScope().declare(element.name, item, boundType, false)

            val afterBody = body.join(scoped).popScope()
            if (afterBody.shouldReturn) return afterBody
            state = afterBody
        }

        return state
    }

    private fun Any.iterator(): Iterator<Any?> = when (this) {
        is Iterable<*> -> iterator()
        is Sequence<*> -> iterator()
        is Map<*, *> -> pairs().iterator()
        else -> error("Expected Iterable, Sequence or Map, but got '${this::class.simpleName}'")
    }
}

// Exceptions
public data class Try(
    val body: Statement,
    val catches: List<Catch>,
    val finallyBody: Statement = Skip
) : Statement() {
    override fun join(machine: MachineState): MachineState {
        val afterBody = body.join(machine)

        val beforeFinally =
            if (afterBody.exceptionType != null) {
                catches.find { catch -> catch.variable.type isAssignableFrom afterBody.exceptionType }
                    ?.let { catchBlock ->
                        catchBlock.body.join(
                            afterBody.pushScope()
                                .declare(
                                    catchBlock.variable.name,
                                    catchBlock.variable.type(afterBody.result),
                                    catchBlock.variable.type,
                                    false
                                )
                                .copy(exceptionType = null, shouldReturn = false)
                        ).popScope()
                    } ?: afterBody
            } else afterBody

        return finallyBody.join(beforeFinally.copy(exceptionType = null, shouldReturn = false)).run {
            if (exceptionType != null) this
            else copy(
                shouldReturn = shouldReturn || beforeFinally.shouldReturn,
                result = if (shouldReturn) result else beforeFinally.result,
                exceptionType = if (shouldReturn) exceptionType else beforeFinally.exceptionType
            )
        }
    }

    public data class Catch(val variable: Variable, val body: Statement)
}

public data class Throw(val exceptionType: Type, val message: Expression) : Statement() {
    override fun join(machine: MachineState): MachineState = message(machine).run {
        if (exceptionType == null) copy(exceptionType = this@Throw.exceptionType, shouldReturn = true, result = Unit)
        else this
    }
}

// Exceptions.
public data class Return(val value: Expression) : Statement() {
    override fun join(machine: MachineState): MachineState = value(machine).copy(shouldReturn = true)
}

// Chain.
public data class Chain(val leftPart: Statement, val rightPart: Statement) : Statement() {
    override fun join(machine: MachineState): MachineState = leftPart.join(machine).run {
        if (shouldReturn) this else rightPart.join(this)
    }
}

public fun Statement.scoped(): Scoped = Scoped(this)

public fun chainOf(vararg statements: Statement): Statement = when (statements.size) {
    0 -> Skip
    1 -> statements[0]
    else -> statements.reduce(::Chain)
}