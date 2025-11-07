package klib.data.query

import com.ionspin.kotlin.bignum.BigNumber
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.serialization.serializers.bignum.BigDecimalSerial
import klib.data.type.serialization.serializers.bignum.BigIntegerSerial
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

public sealed interface Operand

public sealed interface ComparableOperand : Operand {

    public fun isNull(): IsNull = CompareExpression.isNull(this)

    public fun isNotNull(): IsNotNull = CompareExpression.isNotNull(this)

    public infix fun eq(other: ComparableOperand): Equals = CompareExpression.eq(this, other)

    public infix fun <T> eq(other: T): Equals = eq(other.v)

    public infix fun neq(other: ComparableOperand): NotEquals = CompareExpression.neq(this, other)

    public infix fun <T> neq(other: T): NotEquals = neq(other.v)

    public infix fun lt(other: ComparableOperand): LessThan = CompareExpression.lt(this, other)

    public infix fun <T> lt(other: T): LessThan = lt(other.v)

    public infix fun lte(other: ComparableOperand): LessThanEquals =
        CompareExpression.lte(this, other)

    public infix fun <T> lte(other: T): LessThanEquals = lte(other.v)

    public infix fun gt(other: ComparableOperand): GreaterThan = CompareExpression.gt(this, other)

    public infix fun <T> gt(other: T): GreaterThan = gt(other.v)

    public infix fun gte(other: ComparableOperand): GreaterThanEquals =
        CompareExpression.gte(this, other)

    public infix fun <T> gte(other: T): GreaterThanEquals = gte(other.v)

    public fun between(left: ComparableOperand, right: ComparableOperand): Between =
        CompareExpression.between(this, left, right)

    public fun <T> between(left: T, right: T): Between = between(left.v, right.v)

    public infix fun `in`(other: IterableVariable): In = CompareExpression.`in`(this, other)

    public infix fun nin(other: IterableVariable): NotIn = CompareExpression.nin(this, other)
}

public inline infix fun <reified T> ComparableOperand.`in`(other: Iterable<T>): In = `in`(other.v)

public inline infix fun <reified T> ComparableOperand.nin(other: Iterable<T>): NotIn =
    nin(other.v)

@Serializable
public sealed class Variable : ComparableOperand {

    public fun p(alias: String? = null): Projection = Projection(this, alias)
}

@Serializable
public sealed class ComparableVariable : Variable(), ComparableOperand

public sealed interface BooleanOperand : Operand {

    public fun and(vararg values: BooleanOperand): And = LogicExpression.and(this, *values)

    public fun and(vararg values: Boolean): And = and(*values.map(Boolean::v).toTypedArray())

    public fun or(vararg values: BooleanOperand): Or = LogicExpression.or(this, *values)

    public fun or(vararg values: Boolean): Or = or(*values.map(Boolean::v).toTypedArray())

    public fun xor(vararg values: BooleanOperand): XOr = LogicExpression.xor(this, *values)

    public fun xor(vararg values: Boolean): XOr = xor(*values.map(Boolean::v).toTypedArray())

    public fun not(): Not = LogicExpression.not(this)
}

@Serializable
public sealed class BooleanVariable : ComparableVariable(), BooleanOperand

public sealed interface NumberOperand : Operand {

    public fun plus(vararg values: NumberOperand): Plus =
        NumberExpression.plus(this, *values)

    public fun plus(vararg values: UByte): Plus =
        plus(*values.map(UByte::v).toTypedArray())

    public fun plus(vararg values: UShort): Plus =
        plus(*values.map(UShort::v).toTypedArray())

    public fun plus(vararg values: UInt): Plus =
        plus(*values.map(UInt::v).toTypedArray())

    public fun plus(vararg values: ULong): Plus =
        plus(*values.map(ULong::v).toTypedArray())


    public fun plus(vararg values: Number): Plus =
        plus(*values.map(Number::v).toTypedArray())

    public fun plus(vararg values: BigNumber<*>): Plus =
        plus(*values.map(BigNumber<*>::v).toTypedArray())

    public fun minus(vararg values: UByte): Minus =
        minus(*values.map(UByte::v).toTypedArray())

    public fun minus(vararg values: UShort): Minus =
        minus(*values.map(UShort::v).toTypedArray())

    public fun minus(vararg values: UInt): Minus =
        minus(*values.map(UInt::v).toTypedArray())

    public fun minus(vararg values: ULong): Minus =
        minus(*values.map(ULong::v).toTypedArray())

    public fun minus(vararg values: NumberOperand): Minus =
        NumberExpression.minus(this, *values)

    public fun minus(vararg values: Number): Minus =
        minus(*values.map(Number::v).toTypedArray())

    public fun minus(vararg values: BigNumber<*>): Minus =
        minus(*values.map(BigNumber<*>::v).toTypedArray())

    public fun times(vararg values: NumberOperand): Times =
        NumberExpression.times(this, *values)

    public fun times(vararg values: UByte): Times =
        times(*values.map(UByte::v).toTypedArray())

    public fun times(vararg values: UShort): Times =
        times(*values.map(UShort::v).toTypedArray())

    public fun times(vararg values: UInt): Times =
        times(*values.map(UInt::v).toTypedArray())

    public fun times(vararg values: ULong): Times =
        times(*values.map(ULong::v).toTypedArray())

    public fun times(vararg values: Number): Times =
        times(*values.map(Number::v).toTypedArray())

    public fun times(vararg values: BigNumber<*>): Times =
        times(*values.map(BigNumber<*>::v).toTypedArray())

    public fun divide(vararg values: NumberOperand): Divide =
        NumberExpression.divide(this, *values)

    public fun divide(vararg values: UByte): Divide =
        divide(*values.map(UByte::v).toTypedArray())

    public fun divide(vararg values: UShort): Divide =
        divide(*values.map(UShort::v).toTypedArray())

    public fun divide(vararg values: UInt): Divide =
        divide(*values.map(UInt::v).toTypedArray())

    public fun divide(vararg values: ULong): Divide =
        divide(*values.map(ULong::v).toTypedArray())

    public fun divide(vararg values: Number): Divide =
        divide(*values.map(Number::v).toTypedArray())

    public fun divide(vararg values: BigNumber<*>): Divide =
        divide(*values.map(BigNumber<*>::v).toTypedArray())

    public infix fun mod(divisor: NumberOperand): Mod =
        NumberExpression.mod(this, divisor)

    public fun mod(divisor: UByte): Mod = mod(divisor.v)

    public fun mod(divisor: UShort): Mod = mod(divisor.v)

    public fun mod(divisor: UInt): Mod = mod(divisor.v)

    public fun mod(divisor: ULong): Mod = mod(divisor.v)

    public infix fun mod(divisor: Number): Mod = mod(divisor.v)

    public fun mod(divisor: BigNumber<*>): Mod = mod(divisor.v)

    public infix fun pow(power: NumberOperand): Pow = NumberExpression.pow(this, power)

    public fun pow(power: UByte): Pow = pow(power.v)

    public fun pow(power: UShort): Pow = pow(power.v)

    public fun pow(power: UInt): Pow = pow(power.v)

    public fun pow(power: ULong): Pow = pow(power.v)

    public infix fun pow(power: Number): Pow = pow(power.v)

    public fun pow(power: BigNumber<*>): Pow = pow(power.v)

    public fun square(): Square = NumberExpression.square(this)
}

@Serializable
public sealed class NumberVariable : ComparableVariable(), NumberOperand

@Serializable
public sealed class BigNumberVariable : ComparableVariable(), NumberOperand

@Serializable
public sealed class CharVariable : ComparableVariable()

public sealed interface StringOperand : Operand {

    public fun eq(
        other: StringOperand,
        ignoreCase: BooleanOperand = false.v,
        matchAll: BooleanOperand = true.v,
    ): StringEquals =
        StringExpression.eq(this, other, ignoreCase, matchAll)

    public fun eq(other: String, ignoreCase: Boolean, matchAll: Boolean): StringEquals =
        eq(other.v, ignoreCase.v, matchAll.v)

    public infix fun like(pattern: StringOperand): Like = StringExpression.like(this, pattern)

    public infix fun like(pattern: String): Like = like(pattern.v)

    public infix fun notLike(pattern: StringOperand): NotLike =
        StringExpression.notLike(this, pattern)

    public infix fun notLike(pattern: String): NotLike = notLike(pattern.v)

    public fun match(pattern: StringOperand, mode: StringOperand? = null): Match =
        StringExpression.match(this, pattern, mode)

    public fun match(pattern: String, mode: String? = null): Match =
        match(pattern.v, mode?.v)

    public fun regexp(
        pattern: StringOperand,
        ignoreCase: BooleanOperand = false.v,
        matchAll: BooleanOperand = true.v,
    ): Regexp =
        StringExpression.regexp(this, pattern, ignoreCase, matchAll)

    public fun regexp(
        pattern: String,
        ignoreCase: Boolean = false,
        matchAll: Boolean = true
    ): Regexp =
        regexp(pattern.v, ignoreCase.v, matchAll.v)

    public fun ascii(): Ascii = StringExpression.ascii(this)
    public fun charLength(): CharLength = StringExpression.charLength(this)
    public fun lowercase(): Lowercase = StringExpression.lowercase(this)
    public fun uppercase(): Uppercase = StringExpression.uppercase(this)
    public fun substring(
        startIndex: NumberOperand,
        endIndex: NumberOperand? = null
    ): Substring = StringExpression.substring(this, startIndex, endIndex)

    public fun substring(
        startIndex: Int,
        endIndex: Int? = null
    ): Substring = substring(startIndex.v, endIndex?.v)

    public fun concat(vararg values: StringOperand, separator: StringOperand = "".v): Concat =
        StringExpression.concat(this, *values, separator = separator)

    public fun concat(vararg values: String, separator: String = ""): Concat =
        concat(*values.map(String::v).toTypedArray(), separator = separator.v)

    public fun groupConcat(
        distinct: BooleanOperand = false.v,
        separator: StringOperand = "".v,
        orderBy: List<Order> = emptyList(),
    ): GroupConcat = StringExpression.groupConcat(
        this,
        distinct,
        separator,
        orderBy
    )

    public fun groupConcat(
        distinct: Boolean = false,
        separator: String = "",
        orderBy: List<Order> = emptyList(),
    ): GroupConcat = groupConcat(
        distinct.v,
        separator.v,
        orderBy,
    )

    public fun replace(
        variable: StringOperand,
        replacement: StringOperand,
        ignoreCase: BooleanOperand = false.v,
    ): Replace =
        StringExpression.replace(this, variable, replacement, ignoreCase)

    public fun replace(value: String, replacement: String, ignoreCase: Boolean = false): Replace =
        replace(value.v, replacement.v, ignoreCase.v)

    public fun replacePattern(
        pattern: StringOperand,
        replacement: StringOperand,
        ignoreCase: BooleanOperand = false.v,
    ): ReplacePattern =
        StringExpression.replacePattern(this, pattern, replacement, ignoreCase)

    public fun replacePattern(
        pattern: String,
        replacement: String,
        ignoreCase: Boolean = false,
    ): ReplacePattern =
        replacePattern(pattern.v, replacement.v, ignoreCase.v)

    public fun reverse(): Reverse = StringExpression.reverse(this)
    public fun trim(vararg charVariables: CharVariable): Trim =
        StringExpression.trim(this, *charVariables)

    public fun trim(vararg chars: Char): Trim = trim(*chars.map(Char::v).toTypedArray())
    public fun trimStart(vararg charVariables: CharVariable): TrimStart =
        StringExpression.trimStart(this, *charVariables)

    public fun trimStart(vararg chars: Char): TrimStart =
        trimStart(*chars.map(Char::v).toTypedArray())

    public fun trimEnd(vararg charVariables: CharVariable): TrimEnd =
        StringExpression.trimEnd(this, *charVariables)

    public fun trimEnd(vararg chars: Char): TrimEnd =
        trimEnd(*chars.map(Char::v).toTypedArray())

    public fun padStart(length: NumberOperand, vararg charVariables: CharVariable): PadStart =
        StringExpression.padStart(this, length, *charVariables)

    public fun padStart(length: Int, vararg chars: Char): PadStart =
        padStart(length.v, *chars.map(Char::v).toTypedArray())

    public fun padEnd(length: NumberOperand, vararg charVariables: CharVariable): PadEnd =
        StringExpression.padEnd(this, length, *charVariables)

    public fun padEnd(length: Int, vararg chars: Char): PadEnd =
        padEnd(length.v, *chars.map(Char::v).toTypedArray())

    public infix fun left(length: NumberOperand): Left = StringExpression.left(this, length)
    public infix fun left(length: Int): Left = left(length.v)
    public infix fun right(length: NumberOperand): Right = StringExpression.right(this, length)
    public infix fun right(length: Int): Right = right(length.v)
    public infix fun replicate(length: NumberOperand): Replicate =
        StringExpression.replicate(this, length)

    public infix fun replicate(length: Int): Replicate = replicate(length.v)
    public fun indexOf(variable: StringOperand, ignoreCase: BooleanOperand = false.v): IndexOf =
        StringExpression.indexOf(this, variable, ignoreCase)

    public fun indexOf(value: String, ignoreCase: Boolean = false): IndexOf =
        indexOf(value.v, ignoreCase.v)

    public fun indexOfPattern(
        pattern: StringOperand,
        ignoreCase: BooleanOperand = false.v,
    ): IndexOfPattern =
        StringExpression.indexOfPattern(this, pattern, ignoreCase)

    public fun indexOfPattern(pattern: String, ignoreCase: Boolean = false): IndexOfPattern =
        indexOfPattern(pattern.v, ignoreCase.v)

    public fun split(separator: StringOperand, ignoreCase: BooleanOperand = false.v): Split =
        StringExpression.split(this, separator, ignoreCase)

    public fun split(separator: String, ignoreCase: Boolean = false): Split =
        split(separator.v, ignoreCase.v)

    public fun splitPattern(
        separator: StringOperand,
        ignoreCase: BooleanOperand = false.v,
    ): SplitPattern =
        StringExpression.splitPattern(this, separator, ignoreCase)

    public fun splitPattern(separator: String, ignoreCase: Boolean = false): SplitPattern =
        splitPattern(separator.v, ignoreCase.v)
}

@Serializable
public sealed class StringVariable : ComparableVariable(), StringOperand

public sealed interface TimeOperand : Operand {

    public fun time(): Time = TimeExpression.time(this)

    public fun date(): Date = TimeExpression.date(this)

    public fun dateTime(): DateTime = TimeExpression.dateTime(this)

    public fun second(): Second = TimeExpression.second(this)

    public fun minute(): Minute = TimeExpression.minute(this)

    public fun hour(): Hour = TimeExpression.hour(this)

    public fun day(): Day = TimeExpression.day(this)

    public fun month(): Month = TimeExpression.month(this)

    public fun year(): Year = TimeExpression.year(this)

    public infix fun format(format: StringOperand): TimeFormat =
        TimeExpression.format(this, format)

    public infix fun format(format: String): TimeFormat =
        TimeExpression.format(this, format.v)
}

@Serializable
public sealed class TimeVariable : ComparableVariable(), TimeOperand

@Serializable
public sealed class IterableVariable : Variable()

public sealed interface Expression {

    public val arguments: List<Variable>
}

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class CompareExpression : BooleanVariable(), Expression {

    public companion object {

        public fun isNull(variable: ComparableOperand): IsNull =
            IsNull(listOf(variable) as List<Variable>)

        public fun isNotNull(variable: ComparableOperand): IsNotNull =
            IsNotNull(listOf(variable) as List<Variable>)

        public fun eq(left: ComparableOperand, right: ComparableOperand): Equals =
            Equals(listOf(left, right) as List<Variable>)

        public fun neq(left: ComparableOperand, right: ComparableOperand): NotEquals =
            NotEquals(listOf(left, right) as List<Variable>)

        public fun lt(left: ComparableOperand, right: ComparableOperand): LessThan =
            LessThan(listOf(left, right) as List<Variable>)

        public fun lte(left: ComparableOperand, right: ComparableOperand): LessThanEquals =
            LessThanEquals(listOf(left, right) as List<Variable>)

        public fun gt(left: ComparableOperand, right: ComparableOperand): GreaterThan =
            GreaterThan(listOf(left, right) as List<Variable>)

        public fun gte(left: ComparableOperand, right: ComparableOperand): GreaterThanEquals =
            GreaterThanEquals(listOf(left, right) as List<Variable>)

        public fun between(
            variable: ComparableOperand,
            left: ComparableOperand,
            right: ComparableOperand
        ): Between =
            Between(listOf(variable, left, right) as List<Variable>)

        public fun `in`(variable: ComparableOperand, iterable: IterableVariable): In =
            In(listOf(variable, iterable) as List<Variable>)

        public fun nin(variable: ComparableOperand, iterable: IterableVariable): NotIn =
            NotIn(listOf(variable, iterable) as List<Variable>)
    }
}

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class LogicExpression : BooleanVariable(), Expression {

    public companion object {

        public fun and(vararg values: BooleanOperand): And =
            And(values.toList() as List<Variable>)

        public fun or(vararg values: BooleanOperand): Or =
            Or(values.toList() as List<Variable>)

        public fun xor(vararg values: BooleanOperand): XOr =
            XOr(values.toList() as List<Variable>)

        public fun not(value: BooleanOperand): Not =
            Not(listOf(value) as List<Variable>)
    }
}

@Serializable
public data class And(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Or(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class XOr(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Not(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class IsNull(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class IsNotNull(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Equals(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class NotEquals(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class LessThan(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class LessThanEquals(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class GreaterThan(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class GreaterThanEquals(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Between(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class In(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class NotIn(override val arguments: List<Variable>) : LogicExpression()

public interface AggregateOperand : Operand {
    public fun count(): Count = AggregateExpression.count(this)

    public fun <T> max(): Max<T> = AggregateExpression.max(this)

    public fun <T> min(): Min<T> = AggregateExpression.min(this)

    public fun <T> avg(): Avg<T> = AggregateExpression.avg(this)

    public fun <T> sum(): Sum<T> = AggregateExpression.sum(this)
}

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class AggregateExpression<T> : Variable(), Expression {
    public companion object {

        public fun count(variable: AggregateOperand? = null): Count =
            Count(listOfNotNull(variable) as List<Variable>)

        public fun <T> max(variable: AggregateOperand): Max<T> =
            Max(listOf(variable) as List<Variable>)

        public fun <T> min(variable: AggregateOperand): Min<T> =
            Min(listOf(variable) as List<Variable>)

        public fun <T> avg(variable: AggregateOperand): Avg<T> =
            Avg(listOf(variable) as List<Variable>)

        public fun <T> sum(variable: AggregateOperand): Sum<T> =
            Sum(listOf(variable) as List<Variable>)
    }
}

@Serializable
public data class Count(override val arguments: List<Variable>) : AggregateExpression<Long>()

@Serializable
public data class Max<T>(override val arguments: List<Variable>) : AggregateExpression<T>()

@Serializable
public data class Min<T>(override val arguments: List<Variable>) : AggregateExpression<T>()

@Serializable
public data class Avg<T>(override val arguments: List<Variable>) : AggregateExpression<T>()

@Serializable
public data class Sum<T>(override val arguments: List<Variable>) : AggregateExpression<T>()

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class NumberExpression : NumberVariable(), Expression {

    public companion object Companion {
        public fun random(seed: NumberOperand?): Random =
            Random(listOfNotNull(seed) as List<Variable>)

        public fun random(seed: Int?): Random = random(seed?.v)

        public fun plus(vararg values: NumberOperand): Plus =
            Plus(values.toList() as List<Variable>)

        public fun minus(vararg values: NumberOperand): Minus =
            Minus(values.toList() as List<Variable>)

        public fun times(vararg values: NumberOperand): Times =
            Times(values.toList() as List<Variable>)

        public fun divide(vararg values: NumberOperand): Divide =
            Divide(values.toList() as List<Variable>)

        public fun mod(dividend: NumberOperand, divisor: NumberOperand): Mod =
            Mod(listOf(dividend, divisor) as List<Variable>)

        public fun pow(variable: NumberOperand, power: NumberOperand): Pow =
            Pow(listOf(variable, power) as List<Variable>)

        public fun square(variable: NumberOperand): Square =
            Square(listOf(variable) as List<Variable>)
    }
}

@Serializable
public data class Random(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Plus(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Minus(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Times(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Divide(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Mod(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Pow(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Square(override val arguments: List<Variable>) : NumberExpression()

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class TimeExpression : TimeVariable(), Expression {

    public companion object Companion {

        public val now: Now = Now
        public val nowDate: NowDate = NowDate
        public val nowDateTime: NowDateTime = NowDateTime

        public fun time(variable: TimeOperand): Time =
            Time(listOf(variable) as List<Variable>)

        public fun date(variable: TimeOperand): Date =
            Date(listOf(variable) as List<Variable>)

        public fun dateTime(variable: TimeOperand): DateTime =
            DateTime(listOf(variable) as List<Variable>)

        public fun second(variable: TimeOperand): Second =
            Second(listOf(variable) as List<Variable>)

        public fun minute(variable: TimeOperand): Minute =
            Minute(listOf(variable) as List<Variable>)

        public fun hour(variable: TimeOperand): Hour =
            Hour(listOf(variable) as List<Variable>)

        public fun day(variable: TimeOperand): Day =
            Day(listOf(variable) as List<Variable>)

        public fun month(variable: TimeOperand): Month =
            Month(listOf(variable) as List<Variable>)

        public fun year(variable: TimeOperand): Year =
            Year(listOf(variable) as List<Variable>)

        public fun dateTimeOffset(variable: TimeOperand): DateTimeOffset =
            DateTimeOffset(listOf(variable) as List<Variable>)

        public fun format(variable: TimeOperand, format: StringOperand): TimeFormat =
            TimeFormat(listOf(variable, format) as List<Variable>)
    }
}

@Serializable
public data object Now : TimeExpression() {

    override val arguments: List<Variable> = emptyList()
}

@Serializable
public data object NowDate : TimeExpression() {

    override val arguments: List<Variable> = emptyList()
}

@Serializable
public data object NowDateTime : TimeExpression() {

    override val arguments: List<Variable> = emptyList()
}

@Serializable
public data class Time(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Date(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class DateTime(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class DateTimeOffset(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Second(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Minute(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Hour(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Day(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Month(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class Year(override val arguments: List<Variable>) : TimeExpression()

@Serializable
public data class TimeFormat(override val arguments: List<Variable>) :
    TimeExpression()

@Suppress("UNCHECKED_CAST")
@Serializable
public sealed class StringExpression : StringVariable(), Expression {

    public companion object {

        public fun eq(
            left: StringOperand,
            right: StringOperand,
            ignoreCase: BooleanOperand = false.v,
            matchAll: BooleanOperand = true.v,
        ): StringEquals =
            StringEquals(listOf(left, right, ignoreCase, matchAll) as List<Variable>)

        public fun like(
            left: StringOperand,
            right: StringOperand,
        ): Like = Like(listOf(left, right) as List<Variable>)

        public fun notLike(
            left: StringOperand,
            right: StringOperand,
        ): NotLike = NotLike(listOf(left, right) as List<Variable>)

        public fun match(
            left: StringOperand,
            right: StringOperand,
            mode: StringOperand? = null,
        ): Match = Match(listOfNotNull(left, right, mode) as List<Variable>)

        public fun regexp(
            left: StringOperand,
            right: StringOperand,
            ignoreCase: BooleanOperand = false.v,
            matchAll: BooleanOperand = true.v,
        ): Regexp = Regexp(listOf(left, right, ignoreCase, matchAll) as List<Variable>)

        public fun ascii(variable: StringOperand): Ascii = Ascii(listOf(variable) as List<Variable>)

        public fun charLength(variable: StringOperand): CharLength =
            CharLength(listOf(variable) as List<Variable>)

        public fun lowercase(variable: StringOperand): Lowercase =
            Lowercase(listOf(variable) as List<Variable>)

        public fun uppercase(variable: StringOperand): Uppercase =
            Uppercase(listOf(variable) as List<Variable>)

        public fun substring(
            variable: StringOperand,
            startIndex: NumberOperand,
            endIndex: NumberOperand? = null
        ): Substring = Substring(listOfNotNull(variable, startIndex, endIndex) as List<Variable>)

        public fun concat(vararg values: StringOperand, separator: StringOperand = "".v): Concat =
            Concat(listOf(*values, separator) as List<Variable>)

        public fun groupConcat(
            variable: StringOperand,
            distinct: BooleanOperand = false.v,
            separator: StringOperand = "".v,
            orderBy: List<Order> = emptyList(),
        ): GroupConcat = GroupConcat(
            listOf(
                variable,
                distinct,
                separator
            ) as List<Variable>,
            orderBy,
        )

        public fun replace(
            variable: StringOperand,
            pattern: StringOperand,
            replacement: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): Replace =
            Replace(listOf(variable, pattern, replacement, ignoreCase) as List<Variable>)

        public fun replacePattern(
            variable: StringOperand,
            pattern: StringOperand,
            replacement: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): ReplacePattern =
            ReplacePattern(listOf(variable, pattern, replacement, ignoreCase) as List<Variable>)

        public fun reverse(variable: StringOperand): Reverse =
            Reverse(listOf(variable) as List<Variable>)

        public fun trim(variable: StringOperand, vararg trimValues: CharVariable): Trim =
            Trim((listOf(variable) + trimValues.toList()) as List<Variable>)

        public fun trimStart(variable: StringOperand, vararg trimValues: CharVariable): TrimStart =
            TrimStart((listOf(variable) + trimValues.toList()) as List<Variable>)

        public fun trimEnd(variable: StringOperand, vararg trimValues: CharVariable): TrimEnd =
            TrimEnd((listOf(variable) + trimValues.toList()) as List<Variable>)

        public fun padStart(
            variable: StringOperand,
            count: NumberOperand,
            vararg padValues: CharVariable
        ): PadStart =
            PadStart((listOf(variable, count) + padValues.toList()) as List<Variable>)

        public fun padEnd(
            variable: StringOperand,
            count: NumberOperand,
            vararg padValues: CharVariable
        ): PadEnd =
            PadEnd((listOf(variable, count) + padValues.toList()) as List<Variable>)

        public fun left(variable: StringOperand, count: NumberOperand): Left =
            Left(listOf(variable, count) as List<Variable>)

        public fun right(variable: StringOperand, count: NumberOperand): Right =
            Right(listOf(variable, count) as List<Variable>)

        public fun replicate(variable: StringOperand, count: NumberOperand): Replicate =
            Replicate(listOf(variable, count) as List<Variable>)

        public fun indexOf(
            variable: StringOperand,
            pattern: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): IndexOf =
            IndexOf(listOf(variable, pattern, ignoreCase) as List<Variable>)

        public fun indexOfPattern(
            variable: StringOperand,
            pattern: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): IndexOfPattern =
            IndexOfPattern(listOf(variable, pattern, ignoreCase) as List<Variable>)

        public fun space(length: NumberOperand): Space = Space(listOf(length) as List<Variable>)

        public fun space(length: Int): Space = space(length.v)

        public fun split(
            variable: StringOperand,
            separator: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): Split =
            Split(listOf(variable, separator, ignoreCase) as List<Variable>)

        public fun splitPattern(
            variable: StringOperand,
            separator: StringOperand,
            ignoreCase: BooleanOperand = false.v,
        ): SplitPattern =
            SplitPattern(listOf(variable, separator, ignoreCase) as List<Variable>)
    }
}

@Serializable
public data class StringEquals(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Like(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class NotLike(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Match(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Regexp(override val arguments: List<Variable>) : LogicExpression()

@Serializable
public data class Ascii(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class CharLength(override val arguments: List<Variable>) : NumberExpression()

@Serializable
public data class Lowercase(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Uppercase(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Substring(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Concat(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class GroupConcat(
    override val arguments: List<Variable>,
    val orderBy: List<Order> = emptyList(),
) : StringExpression()

@Serializable
public data class Replace(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class ReplacePattern(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Reverse(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Trim(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class TrimStart(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class TrimEnd(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class PadStart(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class PadEnd(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Left(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Right(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Replicate(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class IndexOf(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class IndexOfPattern(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Space(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class Split(override val arguments: List<Variable>) : StringExpression()

@Serializable
public data class SplitPattern(override val arguments: List<Variable>) : StringExpression()

public sealed interface Value<T> {

    public val value: T
}

public sealed interface IterableValue<T> : Value<Iterable<T>>

@Serializable
public data class BooleanValue(override val value: Boolean) : BooleanVariable(), Value<Boolean>

@Serializable
public data class BooleanIterable(override val value: Iterable<Boolean>) :
    IterableVariable(),
    IterableValue<Boolean>

@Serializable
public data class UByteValue(override val value: UByte) : NumberVariable(), Value<UByte>

@Serializable
public data class UByteIterable(override val value: Iterable<UByte>) : IterableVariable(),
    IterableValue<UByte>

@Serializable
public data class UShortValue(override val value: UShort) : NumberVariable(), Value<UShort>

@Serializable
public data class UShortIterable(override val value: Iterable<UShort>) : IterableVariable(),
    IterableValue<UShort>

@Serializable
public data class UIntValue(override val value: UInt) : NumberVariable(), Value<UInt>

@Serializable
public data class UIntIterable(override val value: Iterable<UInt>) : IterableVariable(),
    IterableValue<UInt>

@Serializable
public data class ULongValue(override val value: ULong) : NumberVariable(), Value<ULong>

@Serializable
public data class ULongIterable(override val value: Iterable<ULong>) : IterableVariable(),
    IterableValue<ULong>

@Serializable
public data class ByteValue(override val value: Byte) : NumberVariable(), Value<Byte>

@Serializable
public data class ByteIterable(override val value: Iterable<Byte>) : IterableVariable(),
    IterableValue<Byte>

@Serializable
public data class ShortValue(override val value: Short) : NumberVariable(), Value<Short>

@Serializable
public data class ShortIterable(override val value: Iterable<Short>) : IterableVariable(),
    IterableValue<Short>

@Serializable
public data class IntValue(override val value: Int) : NumberVariable(), Value<Int>

@Serializable
public data class IntIterable(override val value: Iterable<Int>) : IterableVariable(),
    IterableValue<Int>

@Serializable
public data class LongValue(override val value: Long) : NumberVariable(), Value<Long>

@Serializable
public data class LongIterable(override val value: Iterable<Long>) : IterableVariable(),
    IterableValue<Long>

@Serializable
public data class FloatValue(override val value: Float) : NumberVariable(), Value<Float>

@Serializable
public data class FloatIterable(override val value: Iterable<Float>) : IterableVariable(),
    IterableValue<Float>

@Serializable
public data class DoubleValue(override val value: Double) : NumberVariable(), Value<Double>

@Serializable
public data class DoubleIterable(override val value: Iterable<Double>) : IterableVariable(),
    IterableValue<Double>

@Serializable
public data class BigIntegerValue(override val value: BigIntegerSerial) : BigNumberVariable(),
    Value<BigIntegerSerial>

@Serializable
public data class BigIntegerIterable(override val value: Iterable<BigIntegerSerial>) :
    IterableVariable(),
    IterableValue<BigIntegerSerial>

@Serializable
public data class BigDecimalValue(override val value: BigDecimalSerial) : BigNumberVariable(),
    Value<BigDecimalSerial>

@Serializable
public data class BigDecimalIterable(override val value: Iterable<BigDecimalSerial>) :
    IterableVariable(),
    IterableValue<BigDecimalSerial>

@Serializable
public data class CharValue(override val value: Char) : CharVariable(), Value<Char>

@Serializable
public data class CharIterable(override val value: Iterable<Char>) : IterableVariable(),
    IterableValue<Char>

@Serializable
public data class StringValue(override val value: String) : StringVariable(), Value<String>

@Serializable
public data class StringIterable(override val value: Iterable<String>) : IterableVariable(),
    IterableValue<String>

@Serializable
public data class DurationValue(override val value: Duration) : TimeVariable(), Value<Duration>

@Serializable
public data class DurationIterable(override val value: Iterable<Duration>) :
    IterableVariable(),
    IterableValue<Duration>

@Serializable
public data class InstantValue(override val value: Instant) : TimeVariable(), Value<Instant>

@Serializable
public data class InstantIterable(override val value: Iterable<Instant>) :
    IterableVariable(),
    IterableValue<Instant>

@Serializable
public data class LocalTimeValue(override val value: LocalTime) : TimeVariable(), Value<LocalTime>

@Serializable
public data class LocalTimeIterable(override val value: Iterable<LocalTime>) :
    IterableVariable(),
    IterableValue<LocalTime>

@Serializable
public data class LocalDateValue(override val value: LocalDate) : TimeVariable(), Value<LocalDate>

@Serializable
public data class LocalDateIterable(override val value: Iterable<LocalDate>) :
    IterableVariable(),
    IterableValue<LocalDate>

@Serializable
public data class LocalDateTimeValue(override val value: LocalDateTime) : TimeVariable(),
    Value<LocalDateTime>

@Serializable
public data class LocalDateTimeIterable(override val value: Iterable<LocalDateTime>) :
    IterableVariable(),
    IterableValue<LocalDateTime>

@Serializable
public data class UuidValue(override val value: Uuid) : ComparableVariable(), Value<Uuid>

@Serializable
public data class UuidIterable(override val value: Iterable<Uuid>) : IterableVariable(),
    IterableValue<Uuid>

@Serializable
public data object NullValue : Variable(), Value<Nothing?>, ComparableOperand, BooleanOperand,
    NumberOperand,
    StringOperand,
    TimeOperand {

    override val value: Nothing? = null
}

@Serializable
public data object UnitValue : Variable(), Value<Unit>, ComparableOperand, BooleanOperand,
    NumberOperand,
    StringOperand,
    TimeOperand {

    override val value: Unit = Unit
}

@Serializable
public data class ComparableValue<T>(val value: T) : ComparableVariable()

@Serializable
public data class Field(override val value: String) : Variable(),
    Value<String>, ComparableOperand, BooleanOperand, NumberOperand,
    StringOperand, TimeOperand, AggregateOperand {
    public fun p(alias: String? = null, distinct: Boolean = false): Projection =
        Projection(this, alias, distinct)
}

public val KProperty<*>.f: Field
    get() = Field(name)

@Serializable
public data class Projection(
    val variable: Variable,
    val alias: String? = null,
    val distinct: Boolean = false
) : Variable(), AggregateOperand

public val Number.v: NumberVariable
    get() = when (this) {
        is Byte -> v
        is Short -> v
        is Int -> v
        is Long -> v
        is Float -> v
        is Double -> v
        else -> throw IllegalArgumentException("Unknown value $this")
    }

public val BigNumber<*>.v: BigNumberVariable
    get() = when (this) {
        is BigInteger -> v
        is BigDecimal -> v
        else -> throw IllegalArgumentException("Unknown value $this")
    }

public val <T>  T.v: Variable
    get() = when (this) {
        null -> NullValue
        is Boolean -> v
        is UByte -> v
        is UShort -> v
        is UInt -> v
        is ULong -> v
        is Number -> v
        is BigNumber<*> -> v
        is Char -> v
        is String -> v
        is Duration -> v
        is Instant -> v
        is LocalTime -> v
        is LocalDate -> v
        is LocalDateTime -> v
        is Uuid -> v
        else -> throw IllegalArgumentException("Unknown value '$this'")
    }

@Suppress("UNCHECKED_CAST")
public inline val <reified T> Iterable<T>.v: IterableVariable
    get() = when (T::class) {
        Boolean::class -> (this as Iterable<Boolean>).v
        UByte::class -> (this as Iterable<UByte>).v
        UShort::class -> (this as Iterable<UShort>).v
        UInt::class -> (this as Iterable<UInt>).v
        ULong::class -> (this as Iterable<ULong>).v
        Byte::class -> (this as Iterable<Byte>).v
        Short::class -> (this as Iterable<Short>).v
        Int::class -> (this as Iterable<Int>).v
        Long::class -> (this as Iterable<Long>).v
        Float::class -> (this as Iterable<Float>).v
        Double::class -> (this as Iterable<Double>).v
        BigInteger::class -> (this as Iterable<BigInteger>).v
        BigDecimal::class -> (this as Iterable<BigDecimal>).v
        Char::class -> (this as Iterable<Char>).v
        String::class -> (this as Iterable<Boolean>).v
        Duration::class -> (this as Iterable<Duration>).v
        Instant::class -> (this as Iterable<Instant>).v
        LocalTime::class -> (this as Iterable<LocalTime>).v
        LocalDate::class -> (this as Iterable<LocalDate>).v
        LocalDateTime::class -> (this as Iterable<LocalDateTime>).v
        Uuid::class -> (this as Iterable<Uuid>).v
        else -> throw IllegalArgumentException("Unknown type '${T::class.simpleName}'")
    }

///////////////////////////////////////////////////////EXTENSION////////////////////////////////////////////////////////
public val Boolean.v: BooleanValue
    get() = BooleanValue(this)

public fun Boolean.p(alias: String? = null): Projection = v.p(alias)
public fun Boolean.andExp(vararg values: Boolean): And = v.and(*values)
public fun Boolean.orExp(vararg values: Boolean): Or = v.or(*values)
public fun Boolean.xorExp(vararg values: Boolean): XOr = v.xor(*values)
public fun Boolean.notExp(): Not = v.not()

public val Iterable<Boolean>.v: BooleanIterable
    get() = BooleanIterable(this)

public val UByte.v: UByteValue
    get() = UByteValue(this)

public fun UByte.p(alias: String? = null): Projection = v.p(alias)
public fun UByte.plusExp(vararg values: UByte): Plus = v.plus(*values)
public fun UByte.minusExp(vararg values: UByte): Minus = v.minus(*values)
public fun UByte.timesExp(vararg values: UByte): Times = v.times(*values)
public fun UByte.divideExp(vararg values: UByte): Divide = v.divide(*values)
public infix fun UByte.modExp(divisor: UByte): Mod = v.mod(divisor)
public infix fun UByte.powExp(power: UByte): Pow = v.pow(power)
public fun UByte.squareExp(): Square = v.square()

public val Iterable<UByte>.v: UByteIterable
    get() = UByteIterable(this)

public val UShort.v: UShortValue
    get() = UShortValue(this)

public fun UShort.p(alias: String? = null): Projection = v.p(alias)
public fun UShort.plusExp(vararg values: UShort): Plus = v.plus(*values)
public fun UShort.minusExp(vararg values: UShort): Minus = v.minus(*values)
public fun UShort.timesExp(vararg values: UShort): Times = v.times(*values)
public fun UShort.divideExp(vararg values: UShort): Divide = v.divide(*values)
public infix fun UShort.modExp(divisor: UShort): Mod = v.mod(divisor)
public infix fun UShort.powExp(power: UShort): Pow = v.pow(power)
public fun UShort.squareExp(): Square = v.square()

public val Iterable<UShort>.v: UShortIterable
    get() = UShortIterable(this)

public val UInt.v: UIntValue
    get() = UIntValue(this)

public fun UInt.p(alias: String? = null): Projection = v.p(alias)
public fun UInt.plusExp(vararg values: UInt): Plus = v.plus(*values)
public fun UInt.minusExp(vararg values: UInt): Minus = v.minus(*values)
public fun UInt.timesExp(vararg values: UInt): Times = v.times(*values)
public fun UInt.divideExp(vararg values: UInt): Divide = v.divide(*values)
public infix fun UInt.modExp(divisor: UInt): Mod = v.mod(divisor)
public infix fun UInt.powExp(power: UInt): Pow = v.pow(power)
public fun UInt.squareExp(): Square = v.square()

public val Iterable<UInt>.v: UIntIterable
    get() = UIntIterable(this)

public val ULong.v: ULongValue
    get() = ULongValue(this)

public fun ULong.p(alias: String? = null): Projection = v.p(alias)
public fun ULong.plusExp(vararg values: ULong): Plus = v.plus(*values)
public fun ULong.minusExp(vararg values: ULong): Minus = v.minus(*values)
public fun ULong.timesExp(vararg values: ULong): Times = v.times(*values)
public fun ULong.divideExp(vararg values: ULong): Divide = v.divide(*values)
public infix fun ULong.modExp(divisor: ULong): Mod = v.mod(divisor)
public infix fun ULong.powExp(power: ULong): Pow = v.pow(power)
public fun ULong.squareExp(): Square = v.square()

public val Iterable<ULong>.v: ULongIterable
    get() = ULongIterable(this)

public val Byte.v: ByteValue
    get() = ByteValue(this)

public fun Byte.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Byte>.v: ByteIterable
    get() = ByteIterable(this)

public val Short.v: ShortValue
    get() = ShortValue(this)

public fun Short.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Short>.v: ShortIterable
    get() = ShortIterable(this)

public val Int.v: IntValue
    get() = IntValue(this)

public fun Int.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Int>.v: IntIterable
    get() = IntIterable(this)

public val Long.v: LongValue
    get() = LongValue(this)

public fun Long.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Long>.v: LongIterable
    get() = LongIterable(this)

public val Float.v: FloatValue
    get() = FloatValue(this)

public fun Float.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Float>.v: FloatIterable
    get() = FloatIterable(this)

public val Double.v: DoubleValue
    get() = DoubleValue(this)

public fun Double.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Double>.v: DoubleIterable
    get() = DoubleIterable(this)

public fun Number.plusExp(vararg values: Number): Plus = v.plus(*values)
public fun Number.minusExp(vararg values: Number): Minus = v.minus(*values)
public fun Number.timesExp(vararg values: Number): Times = v.times(*values)
public fun Number.divideExp(vararg values: Number): Divide = v.divide(*values)
public infix fun Number.modExp(divisor: Number): Mod = v.mod(divisor)
public infix fun Number.powExp(power: Number): Pow = v.pow(power)
public fun Number.squareExp(): Square = v.square()

public val BigInteger.v: BigIntegerValue
    get() = BigIntegerValue(this)

public val Iterable<BigInteger>.v: BigIntegerIterable
    get() = BigIntegerIterable(this)

public val BigDecimal.v: BigDecimalValue
    get() = BigDecimalValue(this)

public val Iterable<BigDecimal>.v: BigDecimalIterable
    get() = BigDecimalIterable(this)

public fun BigNumber<*>.p(alias: String? = null): Projection = v.p(alias)
public fun BigNumber<*>.plusExp(vararg values: BigNumber<*>): Plus = v.plus(*values)
public fun BigNumber<*>.minusExp(vararg values: BigNumber<*>): Minus = v.minus(*values)
public fun BigNumber<*>.timesExp(vararg values: BigNumber<*>): Times = v.times(*values)
public fun BigNumber<*>.divideExp(vararg values: BigNumber<*>): Divide = v.divide(*values)
public infix fun BigNumber<*>.modExp(divisor: BigNumber<*>): Mod = v.mod(divisor)
public infix fun BigNumber<*>.powExp(power: BigNumber<*>): Pow = v.pow(power)
public fun BigNumber<*>.squareExp(): Square = v.square()

public val Char.v: CharValue
    get() = CharValue(this)

public fun Char.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Char>.v: CharIterable
    get() = CharIterable(this)

public val String.v: StringValue
    get() = StringValue(this)

public val String.f: Field
    get() = Field(this)

public fun String.p(alias: String? = null): Projection = v.p(alias)
public fun String.eqExp(
    other: String,
    ignoreCase: Boolean = false,
    matchAll: Boolean = false
): StringEquals = v.eq(other, ignoreCase, matchAll)

public infix fun String.likeExp(pattern: String): Like = v.like(pattern)
public infix fun String.notLikeExp(pattern: String): Like = v.like(pattern)
public fun String.match(pattern: String, mode: String): Match = v.match(pattern, mode)
public fun String.regexpExp(
    pattern: String,
    ignoreCase: Boolean = false,
    matchAll: Boolean = false
): Regexp = v.regexp(pattern, ignoreCase, matchAll)

public fun String.asciiExp(): Ascii = v.ascii()
public fun String.cherLengthExp(): CharLength = v.charLength()
public fun String.lowercaseExp(): Lowercase = v.lowercase()
public fun String.uppercaseExp(): Uppercase = v.uppercase()
public fun String.substringExp(startIndex: Int, endIndex: Int? = null): Substring =
    v.substring(startIndex, endIndex)

public fun String.replaceExp(value: String, replacement: String, ignoreCase: Boolean): Replace =
    v.replace(value, replacement, ignoreCase)

public fun String.replacePatternExp(
    pattern: String,
    replacement: String,
    ignoreCase: Boolean
): ReplacePattern = v.replacePattern(pattern, replacement, ignoreCase)

public fun String.reverseExp(): Reverse = v.reverse()
public fun String.trimExp(vararg chars: Char): Trim = v.trim(*chars)
public fun String.trimStartExp(vararg chars: Char): TrimStart = v.trimStart(*chars)
public fun String.trimEndExp(vararg chars: Char): TrimEnd = v.trimEnd(*chars)
public fun String.padStartExp(length: Int, vararg chars: Char): PadStart =
    v.padStart(length, *chars)

public fun String.padEndExp(length: Int, vararg chars: Char): PadEnd =
    v.padEnd(length, *chars)

public infix fun String.leftExp(length: Int): Left = v.left(length)
public infix fun String.rightExp(length: Int): Right = v.right(length)
public infix fun String.replicateExp(length: Int): Replicate = v.replicate(length)
public fun String.indexOfExp(pattern: String, ignoreCase: Boolean): IndexOf =
    v.indexOf(pattern, ignoreCase)

public fun String.indexOfPatternExp(pattern: String, ignoreCase: Boolean): IndexOfPattern =
    v.indexOfPattern(pattern, ignoreCase)

public fun String.splitExp(separator: String, ignoreCase: Boolean): Split =
    v.split(separator, ignoreCase)

public fun String.splitPatternExp(separator: String, ignoreCase: Boolean): SplitPattern =
    v.splitPattern(separator, ignoreCase)

public val Iterable<String>.v: StringIterable
    get() = StringIterable(this)

public val Duration.v: DurationValue
    get() = DurationValue(this)

public fun Duration.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Duration>.v: DurationIterable
    get() = DurationIterable(this)

public val Instant.v: InstantValue
    get() = InstantValue(this)

public fun Instant.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Instant>.v: InstantIterable
    get() = InstantIterable(this)

public val LocalTime.v: LocalTimeValue
    get() = LocalTimeValue(this)

public fun LocalTime.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<LocalTime>.v: LocalTimeIterable
    get() = LocalTimeIterable(this)

public val LocalDate.v: LocalDateValue
    get() = LocalDateValue(this)

public fun LocalDate.p(alias: String? = null): Projection = v.p(alias)
public fun LocalDate.timeExp(): Time = v.time()
public fun LocalDate.dateExp(): Date = v.date()
public fun LocalDate.secondExp(): Second = v.second()
public fun LocalDate.minuteExp(): Minute = v.minute()
public fun LocalDate.hourExp(): Hour = v.hour()
public fun LocalDate.dayExp(): Day = v.day()
public fun LocalDate.monthExp(): Month = v.month()
public fun LocalDate.yearExp(): Year = v.year()
public infix fun LocalDate.formatExp(format: String): TimeFormat = v.format(format)

public val Iterable<LocalDate>.v: LocalDateIterable
    get() = LocalDateIterable(this)

public val LocalDateTime.v: LocalDateTimeValue
    get() = LocalDateTimeValue(this)

public fun LocalDateTime.p(alias: String? = null): Projection = v.p(alias)
public fun LocalDateTime.timeExp(): Time = v.time()
public fun LocalDateTime.dateExp(): Date = v.date()
public fun LocalDateTime.secondExp(): Second = v.second()
public fun LocalDateTime.minuteExp(): Minute = v.minute()
public fun LocalDateTime.hourExp(): Hour = v.hour()
public fun LocalDateTime.dayExp(): Day = v.day()
public fun LocalDateTime.monthExp(): Month = v.month()
public fun LocalDateTime.yearExp(): Year = v.year()
public infix fun LocalDateTime.formatExp(format: String): TimeFormat = v.format(format)

public val Iterable<LocalDateTime>.v: LocalDateTimeIterable
    get() = LocalDateTimeIterable(this)

public val Uuid.v: UuidValue
    get() = UuidValue(this)

public fun Uuid.p(alias: String? = null): Projection = v.p(alias)

public val Iterable<Uuid>.v: UuidIterable
    get() = UuidIterable(this)

public fun Any.isNullExp(): IsNull = v.isNull()
public fun Any.isNotNullExp(): IsNotNull = v.isNotNull()

public infix fun <T> T.eqExp(other: T): Equals = v.eq(other)
public infix fun <T> T.neqExp(other: T): NotEquals = v.neq(other)

public infix fun <T : Comparable<T>> T.ltExp(other: T): LessThan = v.lt(other)
public infix fun <T : Comparable<T>> T.lteExp(other: T): LessThanEquals = v.lte(other)
public infix fun <T : Comparable<T>> T.gtExp(other: T): GreaterThan = v.gt(other)
public infix fun <T : Comparable<T>> T.gteExp(other: T): GreaterThanEquals = v.gte(other)
public fun <T : Comparable<T>> T.betweenExp(left: T, right: T): Between = v.between(left, right)
public inline infix fun <reified T : Comparable<T>> T.inExp(iterable: Iterable<T>): In =
    v.`in`(iterable)

public inline infix fun <reified T : Comparable<T>> T.ninExp(iterable: Iterable<T>): NotIn =
    v.nin(iterable)

