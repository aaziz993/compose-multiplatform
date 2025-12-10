package klib.data.type.primitives.number

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.HEX_DIGIT_CHARS
import kotlin.IllegalArgumentException
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.reflect.KClass
import kotlin.text.toInt

public val U_BYTE_DEFAULT: UByte = 0U.toUByte()
public val U_SHORT_DEFAULT: UShort = 0U.toUShort()
public const val U_INT_DEFAULT: UInt = 0U
public const val U_LONG_DEFAULT: ULong = 0UL
public const val BYTE_DEFAULT: Byte = 0.toByte()
public const val SHORT_DEFAULT: Short = 0.toShort()
public const val INT_DEFAULT: Int = 0
public const val LONG_DEFAULT: Long = 0L
public const val FLOAT_DEFAULT: Float = 0F
public const val DOUBLE_DEFAULT: Double = 0.0
public const val CHAR_DEFAULT: Char = ' '
public const val STRING_DEFAULT: String = ""
public val BIG_INTEGER_DEFAULT: BigInteger = BigInteger.ZERO
public val BIG_DECIMAL_DEFAULT: BigDecimal = BigDecimal.ZERO

private val INT_MIN = BigInteger(Int.MIN_VALUE)
private val INT_MAX = BigInteger(Int.MAX_VALUE)
private val LONG_MIN = BigInteger(Long.MIN_VALUE)
private val LONG_MAX = BigInteger(Long.MAX_VALUE)

private val UINT_MIN = BigInteger.ZERO
private val UINT_MAX = BigInteger.parseString(UInt.MAX_VALUE.toString())
private val ULONG_MIN = BigInteger.ZERO
private val ULONG_MAX = BigInteger.parseString(ULong.MAX_VALUE.toString())

public fun String.toNumber(): Any {
    val cleaned = replace("_", "").lowercase()

    // Detect suffix (UL/uL/Ul/ul, U/u, F/f, L/l). Prefer longest match.
    val suffix = when {
        cleaned.endsWith("ul") -> "ul"
        cleaned.endsWith("u") -> "u"
        cleaned.endsWith("f") -> "f"
        cleaned.endsWith("l") -> "l"
        else -> null
    }

    val body = if (suffix != null) cleaned.dropLast(suffix.length) else cleaned

    val hasSign = body.startsWith('+') || body.startsWith('-')
    val isNegative = body.startsWith('-')
    val unsignedRequested = suffix == "u" || suffix == "ul"

    // Body without sign to ease BigInteger parsing for bounds checks.
    val core = if (hasSign) body.drop(1) else body

    val isFloating = '.' in core || 'e' in core

    // Unsigned rules.
    if (unsignedRequested) {
        require(!isNegative) { "Unsigned literal cannot be negative: $this" }
        require(!isFloating) { "Unsigned literal must be an integer (no '.' or exponent): $this" }
        val bi = core.toBigInteger()
        return if (suffix == "ul") {
            require(bi in ULONG_MIN..ULONG_MAX) { "ULong literal out of range: $this" }
            bi.ulongValue()
        }
        else {
            require(bi in UINT_MIN..UINT_MAX) { "UInt literal out of range: $this" }
            bi.uintValue()
        }
    }

    // Floating numbers.
    if (suffix == "f")
        return body.toFloat().also { float -> require(float.isFinite()) { "Float literal out of range: $this" } }
    if (isFloating)
        return body.toDouble().also { double -> require(double.isFinite()) { "Double literal out of range: $this" } }

    // Integers (signed, no unsigned suffix).
    val bi = body.toBigInteger()
    return when {
        suffix == "l" -> {
            require(bi in LONG_MIN..LONG_MAX) { "Long literal out of range: $this" }
            bi.longValue()
        }

        bi in INT_MIN..INT_MAX -> bi.intValue()
        bi in LONG_MIN..LONG_MAX -> bi.longValue()

        else -> bi
    }
}

@Suppress("UNCHECKED_CAST")
public fun <T : Comparable<T>> Any.toNumber(kClass: KClass<T>): T = when (kClass) {
    UByte::class -> toString().toUByte()
    UShort::class -> toString().toUShort()
    UInt::class -> toString().toUInt()
    ULong::class -> toString().toULong()
    Byte::class -> toString().toByte()
    Short::class -> toString().toShort()
    Int::class -> toString().toInt()
    Long::class -> toString().toLong()
    Float::class -> toString().toFloat()
    Double::class -> toString().toDouble()
    else -> throw IllegalArgumentException("Unknown value: $this")
} as T

public inline fun <reified T : Comparable<T>> Any.toNumber(): T =
    toNumber(T::class)

// ////////////////////////////////////////////////////////BYTE/////////////////////////////////////////////////////////
public val UByte.Companion.DEFAULT: UByte
    get() = 0U.toUByte()

public val UByte.Companion.MAX_HALF_VALUE: UByte
    get() = Byte.MAX_VALUE.toUByte()

public fun UByte.signed(): Byte = (this - UByte.MAX_HALF_VALUE - 1u).toByte()

public fun UByte.toIntLSB(): Int = toInt().toLSB()

public fun UByte.toUIntLSB(): UInt = toUInt().toLSB()

public fun UByte.toLongLSB(): Long = toLong().toLSB()

public fun UByte.toULongLSB(): ULong = toULong().toLSB()

// ////////////////////////////////////////////////////////BYTE/////////////////////////////////////////////////////////
public val Byte.Companion.DEFAULT: Byte
    get() = 0.toByte()

public fun Byte.unsigned(): UByte = (this + Byte.MAX_VALUE + 1).toUByte()

public fun Byte.normalize(max: Byte = Byte.MAX_VALUE): Float = toFloat() / max

// Syntactic sugar.
public infix fun Byte.shr(other: Int): Int = toInt() shr other

public infix fun Byte.shl(other: Int): Int = toInt() shl other

// Syntactic sugar.
public infix fun Byte.and(other: Int): Int = toInt() and other

public infix fun Byte.and(other: UInt): UInt = toUInt() and other

// Syntactic sugar.
public infix fun Byte.and(other: Long): Long = toLong() and other

public infix fun Byte.and(other: ULong): ULong = toULong() and other

// Pending `kotlin.experimental.xor` becoming stable
public infix fun Byte.xor(other: Byte): Byte = (toInt() xor other.toInt()).toByte()

public fun Byte.toIntLSB(): Int = this and 0xff

public fun Byte.toUIntLSB(): UInt = this and 0xffu

public fun Byte.toLongLSB(): Long = this and 0xffL

public fun Byte.toULongLSB(): ULong = this and 0xffuL

public fun Byte.toHexString(): String {
    val result = CharArray(2)
    result[0] = HEX_DIGIT_CHARS[this shr 4 and 0xf]
    result[1] = HEX_DIGIT_CHARS[this and 0xf]
    return result.concatToString()
}

// ///////////////////////////////////////////////////////USHORT////////////////////////////////////////////////////////
public val UShort.Companion.DEFAULT: UShort
    get() = 0U.toUShort()

public val UShort.Companion.MAX_HALF_VALUE: UShort
    get() = Short.MAX_VALUE.toUShort()

public fun UShort.signed(): Short = (this - UShort.MAX_HALF_VALUE - 1u).toShort()

// ///////////////////////////////////////////////////////SHORT/////////////////////////////////////////////////////////
public val Short.Companion.DEFAULT: Short
    get() = 0.toShort()

public fun Short.unsigned(): UShort = (this + Short.MAX_VALUE + 1).toUShort()

public fun Short.normalize(max: Short = Short.MAX_VALUE): Float = toFloat() / max

public fun Short.reverseBytes(): Short {
    val i = toInt() and 0xffff
    val reversed = (i and 0xff00 ushr 8) or (i and 0x00ff shl 8)
    return reversed.toShort()
}

// ////////////////////////////////////////////////////////UINT/////////////////////////////////////////////////////////
@Suppress("SameReturnValue")
public val UInt.Companion.DEFAULT: UInt
    get() = 0U

public val UInt.Companion.MAX_HALF_VALUE: UInt
    get() = Int.MAX_VALUE.toUInt()

public fun UInt.signed(): Int = (this - UInt.MAX_HALF_VALUE - 1u).toInt()

public fun UInt.toLSB(): UInt = this and 0xffu

public fun UInt.digits(base: UInt): List<UInt> =
    buildList {
        var n = this@digits
        while (n > 0U) {
            add(n % base)
            n /= base
        }
    }

public fun UInt.assignBits(
    lowIndex: Int,
    highIndex: Int,
    set: Boolean,
): UInt =
    intSetBits(lowIndex, highIndex).toUInt().let {
        if (set) {
            this or it
        }
        else {
            this and it.inv()
        }
    }

public fun UInt.sliceBits(
    lowIndex: Int,
    highIndex: Int,
): UInt = (this and (-1 ushr (Int.SIZE_BITS - highIndex - 1)).toUInt()) shr lowIndex

public fun UInt.sliceByte(lowIndex: Int = 0): UInt = sliceBits(lowIndex, lowIndex + 7)

public fun List<UInt>.toUInt(base: UInt): UInt =
    foldIndexed(0U) { i, acc, v ->
        acc + v * base.toDouble().pow(i).toUInt()
    }

public fun BooleanArray.toUInt(): UInt = map { if (it) 1u else 0u }.foldIndexed(0u) { i, acc, v -> acc or (v shl i) }

public fun ByteArray.toUInt(offset: Int = 0): UInt =
    this[offset].toUIntLSB() or
        (this[offset + 1].toUIntLSB() shl 8) or
        (this[offset + 2].toUIntLSB() shl 16) or
        (this[offset + 3].toUIntLSB() shl 24)

public fun ByteArray.toUInt(
    size: Int,
    offset: Int = 0,
): UInt {
    var value = 0u
    (0 until size).forEach { value = value or (this[offset + it].toUIntLSB() shl (it * 8)) }
    return value
}

// /////////////////////////////////////////////////////////INT/////////////////////////////////////////////////////////
@Suppress("SameReturnValue")
public val Int.Companion.DEFAULT: Int
    get() = 0

public fun Int.unsigned(): UInt = (this + Int.MAX_VALUE + 1).toUInt() + 1u

public fun Int.normalize(max: Int = Int.MAX_VALUE): Float = toFloat() / max

// Syntactic sugar.
public infix fun Int.and(other: Long): Long = toLong() and other

public fun Int.toLSB(): Int = this and 0xff

public fun intSetBits(
    lowIndex: Int,
    highIndex: Int,
): Int = (-1 ushr (Int.SIZE_BITS - (highIndex - lowIndex + 1))) shl lowIndex

public fun Int.assignBits(
    lowIndex: Int,
    highIndex: Int,
    set: Boolean,
): Int =
    intSetBits(lowIndex, highIndex).let {
        if (set) {
            this or it
        }
        else {
            this and it.inv()
        }
    }

public fun Int.sliceBits(
    lowIndex: Int,
    highIndex: Int,
): Int = (this and (-1 ushr (Int.SIZE_BITS - highIndex - 1))) ushr lowIndex

public fun Int.sliceByte(lowIndex: Int = 0): Int = sliceBits(lowIndex, lowIndex + 7)

public fun BooleanArray.toInt(): Int = map { if (it) 1 else 0 }.foldIndexed(0) { i, acc, v -> acc or (v shl i) }

public fun ByteArray.toInt(offset: Int = 0): Int =
    this[offset].toIntLSB() or
        (this[offset + 1].toIntLSB() shl 8) or
        (this[offset + 2].toIntLSB() shl 16) or
        (this[offset + 3].toIntLSB() shl 24)

public fun ByteArray.toInt(
    size: Int,
    offset: Int = 0,
): Int {
    var value = 0
    (0 until size).forEach { value = value or (this[offset + it].toIntLSB() shl (it * 8)) }
    return value
}

public fun Any.toInt(): Int = when (this) {
    is UByte -> toInt()
    is UShort -> toInt()
    is UInt -> toInt()
    is ULong -> toInt()
    is Byte -> toInt()
    is Short -> toInt()
    is Int -> this
    is Long -> toInt()
    is Float -> toInt()
    is Double -> toInt()
    is String -> toInt()

    else -> throw IllegalArgumentException("Expected integer value, but got $this")
}

public fun Int.reverseBytes(): Int = (this and -0x1000000 ushr 24) or
    (this and 0x00ff0000 ushr 8) or
    (this and 0x0000ff00 shl 8) or
    (this and 0x000000ff shl 24)

public infix fun Int.leftRotate(bitCount: Int): Int = (this shl bitCount) or (this ushr (Int.SIZE_BITS - bitCount))

public fun Int.toHexString(): String {
    if (this == 0) return "0" // Required as code below does not handle 0

    val result = CharArray(8)
    result[0] = HEX_DIGIT_CHARS[this shr 28 and 0xf]
    result[1] = HEX_DIGIT_CHARS[this shr 24 and 0xf]
    result[2] = HEX_DIGIT_CHARS[this shr 20 and 0xf]
    result[3] = HEX_DIGIT_CHARS[this shr 16 and 0xf]
    result[4] = HEX_DIGIT_CHARS[this shr 12 and 0xf]
    result[5] = HEX_DIGIT_CHARS[this shr 8 and 0xf]
    result[6] = HEX_DIGIT_CHARS[this shr 4 and 0xf]
    result[7] = HEX_DIGIT_CHARS[this and 0xf]

    // Find the first non-zero index
    var i = 0
    while (i < result.size) {
        if (result[i] != '0') break
        i++
    }

    return result.concatToString(i, result.size)
}

// ///////////////////////////////////////////////////////ULONG/////////////////////////////////////////////////////////
@Suppress("SameReturnValue")
public val ULong.Companion.DEFAULT: ULong
    get() = 0UL

public val ULong.Companion.MAX_HALF_VALUE: ULong
    get() = Long.MAX_VALUE.toULong()

public fun ULong.signed(): Long = (this - ULong.MAX_HALF_VALUE - 1u).toLong()

public fun ULong.toLSB(): ULong = this and 0xffu

public fun ULong.digits(base: ULong = 10UL): List<ULong> =
    buildList {
        var n = this@digits
        while (n > 0UL) {
            add(n % base)
            n /= base
        }
    }

public fun ULong.assignBits(
    lowIndex: Int,
    highIndex: Int,
    set: Boolean,
): ULong =
    longSetBits(lowIndex, highIndex).toULong().let {
        if (set) {
            this or it
        }
        else {
            this and it.inv()
        }
    }

public fun ULong.sliceBits(
    lowIndex: Int,
    highIndex: Int,
): ULong = (this and (-1L ushr (Long.SIZE_BITS - highIndex - 1)).toULong()) shr lowIndex

public fun ULong.sliceByte(lowIndex: Int = 0): ULong = sliceBits(lowIndex, lowIndex + 7)

public fun List<UInt>.toULong(base: ULong): ULong =
    foldIndexed(0UL) { i, acc, v ->
        acc + v * base.toDouble().pow(i).toULong()
    }

public fun BooleanArray.toULong(): ULong =
    map { if (it) 1uL else 0uL }.foldIndexed(0uL) { i, acc, v -> acc or (v shl i) }

public fun ByteArray.toULong(
    offset: Int =
        0,
): ULong =
    this[offset].toULongLSB() or
        (this[offset + 1].toULongLSB() shl 8) or
        (this[offset + 2].toULongLSB() shl 16) or
        (this[offset + 3].toULongLSB() shl 24) or
        (this[offset + 4].toULongLSB() shl 32) or
        (this[offset + 5].toULongLSB() shl 40) or
        (this[offset + 6].toULongLSB() shl 48) or
        (this[offset + 7].toULongLSB() shl 56)

public fun ByteArray.toULong(
    size: Int,
    offset: Int = 0,
): ULong {
    var value = 0uL
    (0 until size).forEach { value = value or (this[offset + it].toULongLSB() shl (it * 8)) }
    return value
}

// ////////////////////////////////////////////////////////LONG/////////////////////////////////////////////////////////
public val Long.Companion.DEFAULT: Long
    get() = 0L

public fun Long.unsigned(): ULong = (this + Long.MAX_VALUE + 1).toULong()

public fun Long.normalize(max: Long = Long.MAX_VALUE): Double = toDouble() / max

public fun Long.toLSB(): Long = this and 0xff

public fun longSetBits(
    lowIndex: Int,
    highIndex: Int,
): Long = (-1L ushr (Int.SIZE_BITS - (highIndex - lowIndex + 1))) shl lowIndex

public fun Long.assignBits(
    lowIndex: Int,
    highIndex: Int,
    set: Boolean,
): Long =
    longSetBits(lowIndex, highIndex).let {
        if (set) {
            this or it
        }
        else {
            this and it.inv()
        }
    }

public fun Long.sliceBits(
    lowIndex: Int,
    highIndex: Int,
): Long = (this and (-1L ushr (Int.SIZE_BITS - highIndex - 1))) ushr lowIndex

public fun Long.sliceByte(lowIndex: Int = 0): Long = sliceBits(lowIndex, lowIndex + 7)

public fun BooleanArray.toLong(): Long = map { if (it) 1L else 0L }.foldIndexed(0L) { i, acc, v -> acc or (v shl i) }

public fun ByteArray.toLong(
    offset: Int =
        0,
): Long =
    this[offset].toLongLSB() or
        (this[offset + 1].toLongLSB() shl 8) or
        (this[offset + 2].toLongLSB() shl 16) or
        (this[offset + 3].toLongLSB() shl 24) or
        (this[offset + 4].toLongLSB() shl 32) or
        (this[offset + 5].toLongLSB() shl 40) or
        (this[offset + 6].toLongLSB() shl 48) or
        (this[offset + 7].toLongLSB() shl 56)

public fun ByteArray.toLong(
    size: Int,
    offset: Int = 0,
): Long {
    var value = 0L
    (0 until size).forEach { value = value or (this[offset + it].toLongLSB() shl (it * 8)) }
    return value
}

public fun Long.reverseBytes(): Long =
    (this and -0x100000000000000L ushr 56) or
        (this and 0x00ff000000000000L ushr 40) or
        (this and 0x0000ff0000000000L ushr 24) or
        (this and 0x000000ff00000000L ushr 8) or
        (this and 0x00000000ff000000L shl 8) or
        (this and 0x0000000000ff0000L shl 24) or
        (this and 0x000000000000ff00L shl 40) or
        (this and 0x00000000000000ffL shl 56)

public infix fun Long.rightRotate(bitCount: Int): Long =
    (this ushr bitCount) or (this shl (Long.SIZE_BITS - bitCount))

public fun Long.toHexString(): String {
    if (this == 0L) return "0" // Required as code below does not handle 0

    val result = CharArray(16)
    result[0] = HEX_DIGIT_CHARS[(this shr 60 and 0xf).toInt()]
    result[1] = HEX_DIGIT_CHARS[(this shr 56 and 0xf).toInt()]
    result[2] = HEX_DIGIT_CHARS[(this shr 52 and 0xf).toInt()]
    result[3] = HEX_DIGIT_CHARS[(this shr 48 and 0xf).toInt()]
    result[4] = HEX_DIGIT_CHARS[(this shr 44 and 0xf).toInt()]
    result[5] = HEX_DIGIT_CHARS[(this shr 40 and 0xf).toInt()]
    result[6] = HEX_DIGIT_CHARS[(this shr 36 and 0xf).toInt()]
    result[7] = HEX_DIGIT_CHARS[(this shr 32 and 0xf).toInt()]
    result[8] = HEX_DIGIT_CHARS[(this shr 28 and 0xf).toInt()]
    result[9] = HEX_DIGIT_CHARS[(this shr 24 and 0xf).toInt()]
    result[10] = HEX_DIGIT_CHARS[(this shr 20 and 0xf).toInt()]
    result[11] = HEX_DIGIT_CHARS[(this shr 16 and 0xf).toInt()]
    result[12] = HEX_DIGIT_CHARS[(this shr 12 and 0xf).toInt()]
    result[13] = HEX_DIGIT_CHARS[(this shr 8 and 0xf).toInt()]
    result[14] = HEX_DIGIT_CHARS[(this shr 4 and 0xf).toInt()]
    result[15] = HEX_DIGIT_CHARS[(this and 0xf).toInt()]

    // Find the first non-zero index
    var i = 0
    while (i < result.size) {
        if (result[i] != '0') break
        i++
    }

    return result.concatToString(i, result.size)
}

// ////////////////////////////////////////////////////////FLOAT////////////////////////////////////////////////////////
@Suppress("SameReturnValue")
public val Float.Companion.DEFAULT: Float
    get() = 0F

public fun Float.partition(): Pair<Int, Float> =
    "$absoluteValue".split(".").let {
        it[0].toInt() to it[1].toFloat()
    }

public fun Float.denormalizeByte(max: Byte = Byte.MAX_VALUE): Byte = (this * max).toInt().toByte()

public fun Float.denormalizeShort(max: Short = Short.MAX_VALUE): Short = (this * max).toInt().toShort()

public fun Float.denormalizeInt(max: Int = Int.MAX_VALUE): Int = (this * max).toInt()

public fun Float.denormalizeLong(max: Long = Long.MAX_VALUE): Long = (this * max).toLong()

// /////////////////////////////////////////////////////////DOUBLE//////////////////////////////////////////////////////
@Suppress("SameReturnValue")
public val Double.Companion.DEFAULT: Double
    get() = 0.0

public fun Double.partition(): Pair<Int, Float> =
    "$absoluteValue".split(".").let {
        it[0].toInt() to ".${it[1]}".toFloat()
    }

public fun Double.denormalizeByte(max: Byte = Byte.MAX_VALUE): Byte = (this * max).toInt().toByte()

public fun Double.denormalizeShort(max: Short = Short.MAX_VALUE): Short = (this * max).toInt().toShort()

public fun Double.denormalizeInt(max: Int = Int.MAX_VALUE): Int = (this * max).toInt()

public fun Double.denormalizeLong(max: Long = Long.MAX_VALUE): Long = (this * max).toLong()

public fun Double.toRadians(): Double = this / 180.0 * PI

public fun Double.toDegrees(): Double = this * 180.0 / PI

// ////////////////////////////////////////////////////BIGINTEGER///////////////////////////////////////////////////////
public val BigInteger.Companion.DEFAULT: BigInteger
    get() = ZERO

public fun BigInteger.Companion.parseOrNull(s: String): BigInteger? = s.runCatching { parseString(this) }.getOrNull()

public fun String.toBigInteger(): BigInteger = BigInteger.parseString(this)
public fun String.toBigIntegerOrNull(): BigInteger? = BigInteger.parseOrNull(this)

public fun BigInteger.normalize(maxValue: BigInteger): BigDecimal =
    BigDecimal.fromBigInteger(this).divide(BigDecimal.fromBigInteger(maxValue))

public fun BigInteger.sliceBits(
    lowIndex: Int,
    highIndex: Int,
): BigInteger = (this and BigInteger.TWO.pow(highIndex)) shr lowIndex

public fun BigInteger.sliceByte(lowIndex: Int = 0): BigInteger = sliceBits(lowIndex, lowIndex + 7)

// /////////////////////////////////////////////////////BIGDECIMAL/////////////////////////////////////////////////////////
public val BigDecimal.Companion.DEFAULT: BigDecimal
    get() = ZERO

public fun BigDecimal.Companion.parseOrNull(s: String): BigDecimal? = s.runCatching { parseString(this) }.getOrNull()

public fun String.toBigDecimal(): BigDecimal = BigDecimal.parseString(this)
public fun String.toBigDecimalOrNull(): BigDecimal? = BigDecimal.parseOrNull(this)

/////////////////////////////////////////////////////NUMBER/////////////////////////////////////////////////////////////
public fun Number.toBigInteger(): BigInteger = toString().toBigInteger()

public fun Any.toBigInteger(): BigInteger = when (this) {
    is BigInteger -> this
    is Byte -> BigInteger(this)
    is Short -> BigInteger(this)
    is Int -> BigInteger(this)
    is Long -> BigInteger(this)
    else -> BigInteger.parseString(toString())
}

public fun Number.toBigDecimal(): BigDecimal = toString().toBigDecimal()

public fun Any.toBigDecimal(): BigDecimal = when (this) {
    is BigDecimal -> this
    is Float -> BigDecimal.fromFloat(this)
    is Double -> BigDecimal.fromDouble(this)
    else -> BigDecimal.parseString(toString())
}

private fun BigDecimal.toNumber(vararg types: KClass<*>, exactRequired: Boolean = true) = when {
    types.any { it == BigDecimal::class } -> this
    types.any { it == BigInteger::class } -> toBigInteger()
    types.any { it == Double::class } -> doubleValue(exactRequired)
    types.any { it == Float::class } -> floatValue(exactRequired)
    types.any { it == Long::class } -> longValue(exactRequired)
    types.any { it == Int::class } -> intValue(exactRequired)
    types.any { it == Short::class } -> shortValue(exactRequired)
    types.any { it == Byte::class } -> byteValue(exactRequired)
    types.any { it == ULong::class } -> ulongValue(exactRequired)
    types.any { it == UInt::class } -> uintValue(exactRequired)
    types.any { it == UShort::class } -> ushortValue(exactRequired)
    types.any { it == UByte::class } -> ubyteValue(exactRequired)
    else -> this
}

private fun Any.numUnaryOperate(exactRequired: Boolean = true, block: (BigDecimal) -> BigDecimal): Any =
    block(toBigDecimal()).toNumber(this::class, exactRequired = exactRequired)

public operator fun Any.unaryMinus(): Any = numUnaryOperate(block = BigDecimal::unaryMinus)

public operator fun Any.inc(): Any = numUnaryOperate(block = BigDecimal::inc)

public operator fun Any.dec(): Any = numUnaryOperate(block = BigDecimal::dec)

private fun Any.numBinaryOperate(
    other: Any,
    exactRequired: Boolean = true,
    block: (BigDecimal, BigDecimal) -> BigDecimal
): Any = block(toBigDecimal(), other.toBigDecimal()).toNumber(this::class, other::class, exactRequired = exactRequired)

public operator fun Any.plus(other: Any): Any = numBinaryOperate(other) { v1, v2 -> v1 + v2 }

public operator fun Any.minus(other: Any): Any = numBinaryOperate(other) { v1, v2 -> v1 - v2 }

public operator fun Any.times(other: Any): Any = numBinaryOperate(other) { v1, v2 -> v1 * v2 }

public operator fun Any.div(other: Any): Any = numBinaryOperate(other, false) { v1, v2 -> v1 / v2 }

public operator fun Any.rem(other: Any): Any = numBinaryOperate(other) { v1, v2 -> v1 % v2 }

public fun Any.pow(exponent: Any): Any = when (exponent) {
    is Int -> toBigDecimal().pow(exponent)
    is Long -> toBigDecimal().pow(exponent)

    else -> throw IllegalArgumentException("Expected Int or Long but got ${exponent::class.simpleName}")
}.toNumber(this::class, exactRequired = false)

public fun Any.abs(): Any = toBigDecimal().abs().toNumber(this::class)

public fun Any.negate(): Any = toBigDecimal().negate().toNumber(this::class)

public fun Any.signum(): Int = toBigDecimal().signum()

public fun Any.compareTo(other: Any): Int = toBigDecimal().compareTo(other.toBigDecimal())

public infix fun Any.lt(other: Any): Boolean = compareTo(other) < 0

public infix fun Any.lte(other: Any): Boolean = compareTo(other) <= 0

public infix fun Any.gt(other: Any): Boolean = compareTo(other) > 0

public infix fun Any.gte(other: Any): Boolean = compareTo(other) >= 0

// ///////////////////////////////////////////////////////ARRAY/////////////////////////////////////////////////////////
public fun UInt.toBitArray(): BooleanArray =
    BooleanArray(UInt.SIZE_BITS) {
        ((this shr it) and 1u) != 0u
    }

public fun UInt.toByteArray(): ByteArray =
    byteArrayOf(
        sliceByte().toByte(),
        sliceByte(8).toByte(),
        sliceByte(16).toByte(),
        sliceByte(24).toByte(),
    )

public fun UInt.toByteArray(size: Int): ByteArray = ByteArray(size) { index -> sliceByte(index * 8).toByte() }

public fun Int.toBitArray(): BooleanArray =
    BooleanArray(Int.SIZE_BITS) {
        ((this ushr it) and 1) != 0
    }

public fun Int.toByteArray(): ByteArray =
    byteArrayOf(
        sliceByte().toByte(),
        sliceByte(8).toByte(),
        sliceByte(16).toByte(),
        sliceByte(24).toByte(),
    )

public fun Int.toByteArray(size: Int): ByteArray = ByteArray(size) { index -> sliceByte(index * 8).toByte() }

public fun ULong.toBitArray(): BooleanArray =
    BooleanArray(ULong.SIZE_BITS) {
        ((this shr it) and 1uL) != 0uL
    }

public fun ULong.toByteArray(): ByteArray =
    byteArrayOf(
        sliceByte().toByte(),
        sliceByte(8).toByte(),
        sliceByte(16).toByte(),
        sliceByte(24).toByte(),
        sliceByte(32).toByte(),
        sliceByte(40).toByte(),
        sliceByte(48).toByte(),
        sliceByte(56).toByte(),
    )

public fun ULong.toByteArray(size: Int): ByteArray = ByteArray(size) { index -> sliceByte(index * 8).toByte() }

public fun Long.toBitArray(): BooleanArray =
    BooleanArray(Long.SIZE_BITS) {
        ((this ushr it) and 1L) != 0L
    }

public fun Long.toByteArray(): ByteArray =
    byteArrayOf(
        sliceByte().toByte(),
        sliceByte(8).toByte(),
        sliceByte(16).toByte(),
        sliceByte(24).toByte(),
        sliceByte(32).toByte(),
        sliceByte(40).toByte(),
        sliceByte(48).toByte(),
        sliceByte(56).toByte(),
    )

public fun Long.toByteArray(size: Int): ByteArray = ByteArray(size) { index -> sliceByte(index * 8).toByte() }

public fun minOf(a: Long, b: Int): Long = minOf(a, b.toLong())

public fun minOf(a: Int, b: Long): Long = minOf(a.toLong(), b)
