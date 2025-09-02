package klib.data.type.primitives.string.tokenization.evaluation

import klib.data.type.cast
import klib.data.type.collections.*
import klib.data.type.collections.list.asMutableList
import klib.data.type.collections.map.asMap
import klib.data.type.collections.map.pairs
import klib.data.type.primitives.*
import kotlin.Pair

public sealed class Expression {
    public infix fun coalesce(other: Expression): Coalesce = Coalesce(this, other)
    public operator fun plus(other: Expression): Plus = Plus(this, other)
    public operator fun minus(other: Expression): Minus = Minus(this, other)
    public operator fun times(other: Expression): Times = Times(this, other)
    public operator fun div(other: Expression): Div = Div(this, other)
    public operator fun rem(other: Expression): Rem = Rem(this, other)
    public infix fun pow(other: Expression): Pow = Pow(this, other)
    public fun notNull(): NotNull = NotNull(this)
    public operator fun unaryMinus(): UnaryMinus = UnaryMinus(this)
    public operator fun not(): Not = Not(this)
    public fun preInc(): PreInc = PreInc(this)
    public operator fun inc(): Inc = Inc(this)
    public fun preDec(): PreDec = PreDec(this)
    public operator fun dec(): Dec = Dec(this)
    public infix fun and(other: Expression): And = And(this, other)
    public infix fun or(other: Expression): Or = Or(this, other)
    public infix fun refEq(other: Expression): RefEquals = RefEquals(this, other)
    public infix fun eq(other: Expression): Equals = Equals(this, other)
    public infix fun refNeq(other: Expression): RefNotEquals = RefNotEquals(this, other)
    public infix fun neq(other: Expression): NotEquals = NotEquals(this, other)
    public infix fun lt(other: Expression): LessThan = LessThan(this, other)
    public infix fun leq(other: Expression): LessOrEqualsThan = LessOrEqualsThan(this, other)
    public infix fun gt(other: Expression): GreaterThan = GreaterThan(this, other)
    public infix fun geq(other: Expression): GreaterOrEqualsThan = GreaterOrEqualsThan(this, other)

    public abstract operator fun invoke(machine: MachineState): MachineState
}

public data class StatementExpression(val body: Statement) : Expression() {
    override fun invoke(machine: MachineState): MachineState {
        val entered = machine.pushScope()

        val after = body.join(entered)

        return after.copy(
            scopes = machine.scopes,
            functionScopes = machine.functionScopes
        )
    }
}

public data class Literal(val value: Any?) : Expression() {
    override fun invoke(machine: MachineState): MachineState = machine.copy(result = value)
}

public data class StringLiteral(val value: String) : Expression() {
    override fun invoke(machine: MachineState): MachineState = machine.copy(
        result = value.substitute(
            SubstituteOption.INTERPOLATE,
            SubstituteOption.INTERPOLATE_BRACES,
            SubstituteOption.DEEP_INTERPOLATION,
            SubstituteOption.ESCAPE_DOLLARS,
            SubstituteOption.EVALUATE,
            SubstituteOption.ESCAPE_BACKSLASHES,
            getter = { path ->
                machine[path.first()]?.deepGetOrNull(*path.drop(1).toTypedArray())?.second
            })
    )
}

public data class Variable(val name: String, val type: Type) : Expression() {
    override fun invoke(machine: MachineState): MachineState = machine.copy(result = machine[name])

    override fun toString(): String = "$name:$type"
}

public object UnitLiteral : Expression() {
    override fun invoke(machine: MachineState): MachineState = machine.copy(result = Unit)
}

public data class Pair(val first: Expression, val second: Expression) : Expression() {
    override fun invoke(machine: MachineState): MachineState {
        var currentMachine = machine

        currentMachine = first(currentMachine)
        if (currentMachine.shouldReturn) return currentMachine

        val key = currentMachine.result

        currentMachine = second(currentMachine)
        if (currentMachine.shouldReturn) return currentMachine

        return currentMachine.copy(result = key to currentMachine.result)
    }
}

public sealed class Call(public val arguments: List<Expression>) : Expression() {
    protected open fun operate(arguments: List<Any?>): Any? = throw UnsupportedOperationException()

    protected open fun operate(machine: MachineState, arguments: List<Any?>): MachineState =
        machine.copy(result = operate(arguments))


    protected open fun shouldInvokeNext(arguments: List<Any?>): Boolean = true

    override fun invoke(machine: MachineState): MachineState {
        val list = ArrayList<Any?>(arguments.size)

        var currentMachine = machine

        for (argument in arguments) {
            currentMachine = argument(currentMachine)
            if (currentMachine.shouldReturn) return currentMachine

            list.add(currentMachine.result)

            if (!shouldInvokeNext(list)) break
        }


        return operate(currentMachine, list)
    }
}

public class TypeCall(public val type: Type, arguments: List<Expression>) : Call(arguments) {
    override fun operate(arguments: List<Any?>): Any? = type(*arguments.toTypedArray())
}

public class FunctionCall(public val name: String, arguments: List<Expression>) : Call(arguments) {
    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState {
        val argTypes: List<Type> = this.arguments.mapIndexed { i, argument ->
            if (argument is Variable) {
                val type = machine.getDeclared(argument.name).type
                if (type != Type.UNDEFINED) return@mapIndexed type
            }
            arguments[i]?.let { it::class.simpleName!!.toType() } ?: Type.ANY_Q
        }

        val function = machine.getFunction(name, argTypes)

        var inner = machine.pushScope()

        function.parameters.zip(arguments).forEach { (param, value) ->
            inner = inner.declare(param.name, value, param.type, false)
        }

        val after = function.body.join(inner).popScope()

        return after.copy(scopes = machine.scopes)
    }
}

public sealed class Unary(operand: Expression) : Call(listOf(operand))

public class NotNull(operand: Expression) : Unary(operand) {
    override fun operate(arguments: List<Any?>): Any = arguments[0]!!
}

public class UnaryMinus(operand: Expression) : Unary(operand) {
    override fun operate(arguments: List<Any?>): Any? = -arguments[0]!!
}

public class PreInc(operand: Expression) : Unary(operand) {
    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState {
        val receiver = this.arguments[0]

        require(receiver is Variable) {
            "Only variables can be incremented, got '${arguments[0]!!::class.simpleName}'"
        }

        val old = arguments[0] ?: error("Cannot pre-increment a null value")
        val new = old.inc()

        return machine.set(receiver.name, new).copy(result = new)
    }
}

public class Inc(operand: Expression) : Unary(operand) {
    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState {
        val receiver = this.arguments[0]

        require(receiver is Variable) {
            "Only variables can be incremented, got '${arguments[0]!!::class.simpleName}'"
        }

        val old = arguments[0] ?: error("Cannot increment a null value")
        val new = old.inc()

        return machine.set(receiver.name, new).copy(result = old) // return OLD
    }
}

public class PreDec(operand: Expression) : Unary(operand) {
    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState {
        val receiver = this.arguments[0]

        require(receiver is Variable) {
            "Only variables can be decremented, got '${arguments[0]!!::class.simpleName}'"
        }

        val old = arguments[0] ?: error("Cannot pre-decrement a null value")
        val new = old.dec()

        return machine.set(receiver.name, new).copy(result = new)
    }
}

public class Dec(operand: Expression) : Unary(operand) { // postfix --
    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState {
        val receiver = this.arguments[0]

        require(receiver is Variable) {
            "Only variables can be decremented, got '${arguments[0]!!::class.simpleName}'"
        }

        val old = arguments[0] ?: error("Cannot decrement a null value")
        val new = old.dec()

        return machine.set(receiver.name, new).copy(result = old) // return OLD
    }
}

public class Not(operand: Expression) : Unary(operand) {
    override fun operate(arguments: List<Any?>): Any? = !(arguments[0] as Boolean)
}

public sealed class Binary(left: Expression, right: Expression) : Call(listOf(left, right))

public class Coalesce(left: Expression, right: Expression) : Binary(left, right) {
    override fun shouldInvokeNext(arguments: List<Any?>): Boolean = arguments.last() == null

    override fun operate(arguments: List<Any?>): Any? = arguments.last()
}

public class Plus(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! + arguments[1]!!
}

public class Minus(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! - arguments[1]!!
}

public class Times(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! * arguments[1]!!
}

public class Div(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! / arguments[1]!!
}

public class Rem(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! % arguments[1]!!
}

public class Pow(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!!.pow(arguments[1]!!)
}

public class And(left: Expression, right: Expression) : Binary(left, right) {
    override fun shouldInvokeNext(arguments: List<Any?>): Boolean = arguments.last() == true

    override fun operate(arguments: List<Any?>): Any? = arguments[0] as Boolean && arguments[1] as Boolean
}

public class Or(left: Expression, right: Expression) : Binary(left, right) {
    override fun shouldInvokeNext(arguments: List<Any?>): Boolean = arguments.last() == false

    override fun operate(arguments: List<Any?>): Any? = arguments[0] as Boolean || arguments[1] as Boolean
}

public class Equals(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0] == arguments[1]
}

public class RefEquals(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0] === arguments[1]
}

public class NotEquals(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0] != arguments[1]
}

public class RefNotEquals(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0] !== arguments[1]
}

public class LessThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! lt arguments[1]!!
}

public class LessOrEqualsThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! lte arguments[1]!!
}

public class GreaterThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! gt arguments[1]!!
}

public class GreaterOrEqualsThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]!! gte arguments[1]!!
}

public open class Member(
    arguments: List<Expression>,
    public val optional: Boolean
) : Call(arguments) {
    protected fun <T : Any> T?.runSafely(block: T.() -> Any?): Any? =
        if (this == null && optional) null else this!!.block()
}

// Pair.
public class First(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Pair<*, *>).first
    }
}

public class Second(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Pair<*, *>).second
    }
}

// Number.
public class ToUByte(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(UByte::class)
    }
}

public class ToUShort(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(UShort::class)
    }
}

public class ToUInt(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(UInt::class)
    }
}

public class ToULong(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(ULong::class)
    }
}

public class ToByte(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Byte::class)
    }
}

public class ToShort(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Short::class)
    }
}

public class ToInt(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Int::class)
    }
}

public class ToLong(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Long::class)
    }
}

public class ToFloat(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Float::class)
    }
}

public class ToDouble(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        toNumber(Double::class)
    }
}

// Collection.
public class Get(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        getOrNull(arguments[1])
    }
}

public class Set(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        this[arguments[1]] = arguments[2]
    }
}

public class ToList(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Iterable<*>).toList()
    }
}

public class ToMutableList(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Iterable<*>).toMutableList()
    }
}

public class ToMap(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    @Suppress("UNCHECKED_CAST")
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Iterable<Pair<*, *>>).toMap()
    }
}

// List.
public class Add(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMutableList<Any?>().runSafely {
        if (arguments.size > 2) add(arguments[1] as Int, arguments[2]) else add(arguments[1])
    }
}

public class AddAll(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMutableList<Any?>().runSafely {
        if (arguments.size > 2) addAll(arguments[1] as Int, arguments[2]!!.cast()) else addAll(arguments[1]!!.cast())
    }
}

public class Remove(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        remove(arguments[1])
    }
}

public class Clear(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely(Any::clear)
}

public class Size(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely(Any::size)
}

// Map.
public class Pairs(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMap<Any?, Any?>().runSafely(Map<*, *>::pairs)
}

public class Keys(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMap<Any?, Any?>().runSafely(Map<*, *>::keys)
}

public class Values(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMap<Any?, Any?>().runSafely(Map<*, *>::values)
}

public class ToMutableMap(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Map<*, *>).toMutableMap()
    }
}

// Exception.
public class Cause(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Throwable).cause
    }
}

public class Message(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Throwable).message
    }
}

public class StackTraceToString(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runSafely {
        (this as Throwable).stackTraceToString()
    }
}