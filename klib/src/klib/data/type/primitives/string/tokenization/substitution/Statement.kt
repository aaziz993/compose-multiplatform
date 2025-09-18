package klib.data.type.primitives.string.tokenization.substitution

public sealed class Statement {
    public open operator fun invoke(machine: MachineState): MachineState = machine
}

public data class Scoped(val statement: Statement) : Statement() {
    override fun invoke(machine: MachineState): MachineState = statement(machine.pushScope()).popScope()
}

public data class ExpressionStatement(val expression: Expression) : Statement() {
    override fun invoke(machine: MachineState): MachineState = expression(machine)
}

// Println.
public data class Println(val message: Expression) : Statement() {
    override fun invoke(machine: MachineState): MachineState = message(machine).run {
        if (control != Control.NORMAL) this else copy(log = log + result.toString(), result = Unit)
    }
}

// Skip.
public object Skip : Statement() {
    override fun invoke(machine: MachineState): MachineState = machine.copy(result = Unit)
}

public data class Declare(
    val variable: Variable,
    val value: Expression?,
    val mutable: Boolean
) : Statement() {
    override fun invoke(machine: MachineState): MachineState {
        if (value == null) {
            val boundType = if (variable.type == Type.UNDEFINED) Type.ANY_Q else variable.type
            return machine.declare(variable.name, value, boundType, mutable)
        }

        val after = value.invoke(machine)
        if (after.control != Control.NORMAL) return after

        val boundType = variable.type.takeUnless { type -> type == Type.UNDEFINED }
            ?: after.result?.let { it::class.simpleName!!.toType() }
            ?: Type.ANY_Q

        return after.declare(variable.name, after.result, boundType, mutable)
    }
}

public data class Assign(val assignee: Expression, val value: Expression) : Statement() {
    override fun invoke(machine: MachineState): MachineState = when (assignee) {
        is Variable -> value(machine).run {
            if (control != Control.NORMAL) this else machine.set(assignee.name, result)
        }

        is Get -> ExpressionStatement(Set(assignee.arguments + value, assignee.optional))(machine)

        else -> error("Invalid assignment target")
    }
}

public data class DeclareFunction(val function: Function) : Statement() {
    override fun invoke(machine: MachineState): MachineState = machine.declareFunction(function)
}

// If-else.
public data class If(
    val condition: Expression,
    val thenBody: Statement,
    val elseBody: Statement
) : Statement() {
    override fun invoke(machine: MachineState): MachineState = condition(machine).run {
        when {
            control != Control.NORMAL -> this
            result as Boolean -> thenBody(this)
            else -> elseBody(this)
        }
    }
}

public data class While(
    val label: String? = null,
    val condition: Expression,
    val body: Statement
) : Statement() {
    override tailrec fun invoke(machine: MachineState): MachineState {
        var state = condition(machine)

        when (state.control) {
            is Control.BREAK -> return if (state.control.label == label) state.copy(control = Control.NORMAL) else state

            is Control.CONTINUE -> {
                if (state.control.label == label) state = state.copy(control = Control.NORMAL) else return state
            }

            Control.RETURN -> return state

            else -> Unit
        }

        return if (state.result as Boolean) this(body(state)) else state
    }
}

public data class Break(val label: String? = null) : Statement() {
    override fun invoke(machine: MachineState): MachineState =
        machine.copy(control = Control.BREAK(label), result = Unit)
}

public data class Continue(val label: String? = null) : Statement() {
    override fun invoke(machine: MachineState): MachineState =
        machine.copy(control = Control.CONTINUE(label), result = Unit)
}

// Exceptions
public data class Try(
    val body: Statement,
    val catches: List<Catch>,
    val finallyBody: Statement = Skip
) : Statement() {
    override fun invoke(machine: MachineState): MachineState {
        val afterBody = body(machine)

        val beforeFinally =
            if (afterBody.exceptionType == null) afterBody else {
                catches.find { catch -> catch.variable.type isAssignableFrom afterBody.exceptionType }
                    ?.let { catchBlock ->
                        catchBlock.body(
                            afterBody.pushScope().declare(
                                catchBlock.variable.name,
                                catchBlock.variable.type(afterBody.result),
                                catchBlock.variable.type,
                                false
                            ).copy(exceptionType = null, control = Control.NORMAL)
                        ).popScope()
                    } ?: afterBody
            }

        return finallyBody(beforeFinally.copy(exceptionType = null, control = Control.NORMAL)).run {
            if (exceptionType != null) this
            else copy(
                control = if (control != Control.NORMAL || beforeFinally.control != Control.NORMAL) Control.RETURN else Control.NORMAL,
                result = if (control != Control.NORMAL) result else beforeFinally.result,
                exceptionType = if (control != Control.NORMAL) exceptionType else beforeFinally.exceptionType
            )
        }
    }

    public data class Catch(val variable: Variable, val body: Statement)
}

public data class Throw(val exceptionType: Type, val message: Expression) : Statement() {
    override fun invoke(machine: MachineState): MachineState = message(machine).run {
        if (exceptionType == null) copy(exceptionType = this@Throw.exceptionType, control = Control.RETURN) else this
    }
}

// Exceptions.
public data class Return(val value: Expression) : Statement() {
    override fun invoke(machine: MachineState): MachineState = value(machine).copy(control = Control.RETURN)
}

// Chain.
public data class Chain(val leftPart: Statement, val rightPart: Statement) : Statement() {
    override fun invoke(machine: MachineState): MachineState = leftPart(machine).run {
        if (control != Control.NORMAL) this else rightPart(this)
    }
}

public fun Statement.scoped(): Statement = Scoped(this)

public fun chainOf(vararg statements: Statement): Statement = when (statements.size) {
    0 -> Skip
    1 -> statements[0]
    else -> statements.reduce(::Chain)
}
