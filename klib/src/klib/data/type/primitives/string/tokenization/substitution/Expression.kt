package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.cast
import klib.data.type.collections.*
import kotlin.collections.plus
import kotlin.collections.minus
import klib.data.type.collections.list.asList
import klib.data.type.collections.list.asMutableList
import klib.data.type.collections.list.drop
import klib.data.type.collections.map.asMap
import klib.data.type.collections.map.pairs
import klib.data.type.primitives.number.dec
import klib.data.type.primitives.number.div
import klib.data.type.primitives.number.gt
import klib.data.type.primitives.number.gte
import klib.data.type.primitives.number.inc
import klib.data.type.primitives.number.lt
import klib.data.type.primitives.number.lte
import klib.data.type.primitives.number.minus
import klib.data.type.primitives.number.plus
import klib.data.type.primitives.number.pow
import klib.data.type.primitives.number.rem
import klib.data.type.primitives.number.times
import klib.data.type.primitives.number.toNumber
import klib.data.type.primitives.number.unaryMinus
import kotlin.Pair
import kotlin.collections.Iterator
import kotlin.reflect.KClass

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

        val after = body.invoke(entered)

        return after.copy(
            scopes = machine.scopes,
            functionScopes = machine.functionScopes,
        )
    }
}

public data class Literal(val value: Any?) : Expression() {

    override fun invoke(machine: MachineState): MachineState = machine.copy(result = value)
}

public data class StringLiteral(val value: String) : Expression() {

    override fun invoke(machine: MachineState): MachineState = machine.copy(
        result = value.substitute(interpolate = true, getter = { path ->
            machine[path.first().toString()]?.deepGet(*path.drop().toTypedArray())?.second
        }),
    )
}

public data class Variable(val name: String, val type: Type) : Expression() {

    override fun invoke(machine: MachineState): MachineState = machine.copy(result = machine[name])

    override fun toString(): String = "$name:$type"
}

public data class Reference(val path: List<Any?>) : Expression() {

    override fun invoke(machine: MachineState): MachineState = machine.copy(result = machine.input(path))

    override fun toString(): String = path.joinToString(".")
}

public object UnitLiteral : Expression() {

    override fun invoke(machine: MachineState): MachineState = machine.copy(result = Unit)
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
            if (currentMachine.control != Control.NORMAL) return currentMachine

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

        val after = function.body.invoke(inner).popScope()

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

public class Pair(first: Expression, second: Expression) : Binary(first, second) {

    override fun operate(machine: MachineState, arguments: List<Any?>): MachineState =
        machine.copy(result = arguments[0] to arguments[1])
}

public class Coalesce(left: Expression, right: Expression) : Binary(left, right) {

    override fun shouldInvokeNext(arguments: List<Any?>): Boolean = arguments.last() == null

    override fun operate(arguments: List<Any?>): Any? = arguments.last()
}

public class Plus(left: Expression, right: Expression) : Binary(left, right) {

    override fun operate(arguments: List<Any?>): Any? = when {
        arguments.any { argument -> argument is String } -> arguments.joinToString("")
        arguments.any { argument -> argument is List<*> } -> arguments.reduce { a, b -> a!!.asList + b!!.asList }
        arguments.any { argument -> argument is Map<*, *> } -> arguments.reduce { a, b -> a!!.asMap<Any?, Any?>() + b!!.asMap<Any?, Any?>() }

        else -> arguments[0]!! + arguments[1]!!
    }
}

public class Minus(left: Expression, right: Expression) : Binary(left, right) {

    override fun operate(arguments: List<Any?>): Any? = when {
        arguments.any { argument -> argument is String } -> arguments.joinToString("")
        arguments.any { argument -> argument is List<*> } -> arguments.reduce { a, b -> a!!.asList - b!!.asList }
        arguments.any { argument -> argument is Map<*, *> } -> arguments.reduce { a, b -> a!!.asMap<Any?, Any?>() - b!!.asMap<Any?, Any?>() }

        else -> arguments[0]!! - arguments[1]!!
    }
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

    protected fun <T : Any> T?.runNullSafely(block: T.() -> Any?): Any? =
        if (this == null && optional) null else this!!.block()
}

// Any.
public class Class(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        this::class
    }
}

public class SimpleName(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as KClass<*>).simpleName
    }
}

// Pair.
public class First(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Pair<*, *>).first
    }
}

public class Second(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Pair<*, *>).second
    }
}

// Number.
public class ToUByte(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(UByte::class)
    }
}

public class ToUShort(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(UShort::class)
    }
}

public class ToUInt(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(UInt::class)
    }
}

public class ToULong(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(ULong::class)
    }
}

public class ToByte(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Byte::class)
    }
}

public class ToShort(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Short::class)
    }
}

public class ToInt(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Int::class)
    }
}

public class ToLong(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Long::class)
    }
}

public class ToFloat(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Float::class)
    }
}

public class ToDouble(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        toNumber(Double::class)
    }
}

// Collection.
public class Get(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        getOrNull(arguments[1])
    }
}

public class Set(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        this[arguments[1]] = arguments[2]
    }
}

public class ToList(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Iterable<*>).toList()
    }
}

public class ToMutableList(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Iterable<*>).toMutableList()
    }
}

public class ToMap(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    @Suppress("UNCHECKED_CAST")
    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Iterable<Pair<*, *>>).toMap()
    }
}

public class Iterator(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        iterator()
    }

    private fun Any.iterator(): Iterator<Any?> =
        when (this) {
            is Iterable<*> -> iterator()
            is Sequence<*> -> iterator()
            is Map<*, *> -> pairs().iterator()
            else -> throw IllegalArgumentException("Expected Iterable, Sequence or Map, but got '${this::class.simpleName}'")
        }
}

public class HasNext(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Iterator<*>).hasNext()
    }
}

public class Next(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Iterator<*>).next()
    }
}

public class Size(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely(Any::size)
}

// List.
public class Add(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMutableList<Any?>().runNullSafely {
        if (arguments.size > 2) add(arguments[1] as Int, arguments[2]) else add(arguments[1])
    }
}

public class AddAll(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMutableList<Any?>().runNullSafely {
        if (arguments.size > 2) addAll(arguments[1] as Int, arguments[2]!!.cast()) else addAll(arguments[1]!!.cast())
    }
}

public class Remove(
    arguments: List<Expression>,
    optional: Boolean
) : Member(arguments, optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        remove(arguments[1])
    }
}

public class Clear(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely(Any::clear)
}

// Map.
public class Pairs(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? =
        arguments[0]?.asMap<Any?, Any?>().runNullSafely(Map<*, *>::pairs)
}

public class Keys(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0]?.asMap<Any?, Any?>().runNullSafely(Map<*, *>::keys)
}

public class Values(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? =
        arguments[0]?.asMap<Any?, Any?>().runNullSafely(Map<*, *>::values)
}

public class ToMutableMap(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Map<*, *>).toMutableMap()
    }
}

// Exception.
public class Cause(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Throwable).cause
    }
}

public class Message(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Throwable).message
    }
}

public class StackTraceToString(
    receiver: Expression,
    optional: Boolean
) : Member(listOf(receiver), optional) {

    override fun operate(arguments: List<Any?>): Any? = arguments[0].runNullSafely {
        (this as Throwable).stackTraceToString()
    }
}
