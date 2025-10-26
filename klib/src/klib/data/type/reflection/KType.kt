package klib.data.type.reflection

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.bimap.biMapOf
import klib.data.type.primitives.BIG_DECIMAL_DEFAULT
import klib.data.type.primitives.BIG_INTEGER_DEFAULT
import klib.data.type.primitives.BOOLEAN_DEFAULT
import klib.data.type.primitives.BYTE_DEFAULT
import klib.data.type.primitives.CHAR_DEFAULT
import klib.data.type.primitives.time.DATE_PERIOD_DEFAULT
import klib.data.type.primitives.time.DATE_TIME_PERIOD_DEFAULT
import klib.data.type.primitives.DOUBLE_DEFAULT
import klib.data.type.primitives.time.DURATION_DEFAULT
import klib.data.type.primitives.FLOAT_DEFAULT
import klib.data.type.primitives.INT_DEFAULT
import klib.data.type.primitives.time.LOCAL_DATE_DEFAULT
import klib.data.type.primitives.time.LOCAL_DATE_TIME_DEFAULT
import klib.data.type.primitives.time.LOCAL_TIME_DEFAULT
import klib.data.type.primitives.LONG_DEFAULT
import klib.data.type.primitives.SHORT_DEFAULT
import klib.data.type.primitives.STRING_DEFAULT
import klib.data.type.primitives.UUID_DEFAULT
import klib.data.type.primitives.U_BYTE_DEFAULT
import klib.data.type.primitives.U_INT_DEFAULT
import klib.data.type.primitives.U_LONG_DEFAULT
import klib.data.type.primitives.U_SHORT_DEFAULT
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid

public val KType.kClass: KClass<*>
    get() = classifier as KClass<*>

private val PRIMITIVE_KTYPES = biMapOf(
    typeOf<Boolean>() to typeOf<Boolean?>(),
    typeOf<UByte>() to typeOf<UByte?>(),
    typeOf<UShort>() to typeOf<UShort?>(),
    typeOf<UInt>() to typeOf<UInt?>(),
    typeOf<ULong>() to typeOf<ULong?>(),
    typeOf<Byte>() to typeOf<Byte?>(),
    typeOf<Short>() to typeOf<Short?>(),
    typeOf<Int>() to typeOf<Int?>(),
    typeOf<Long>() to typeOf<Long?>(),
    typeOf<Float>() to typeOf<Float?>(),
    typeOf<Double>() to typeOf<Double?>(),
    typeOf<BigInteger>() to typeOf<BigInteger?>(),
    typeOf<BigDecimal>() to typeOf<BigDecimal?>(),
    typeOf<Char>() to typeOf<Char?>(),
    typeOf<String>() to typeOf<String?>(),
    typeOf<Duration>() to typeOf<Duration?>(),
    typeOf<Instant>() to typeOf<Instant?>(),
    typeOf<LocalTime>() to typeOf<LocalTime?>(),
    typeOf<LocalDate>() to typeOf<LocalDate?>(),
    typeOf<LocalDateTime>() to typeOf<LocalDateTime?>(),
    typeOf<Uuid>() to typeOf<Uuid?>(),
)

public fun KType.withNullability(nullable: Boolean): KType =
    if (nullable) {
        if (isMarkedNullable) this else PRIMITIVE_KTYPES[this]
    }
    else {
        if (isMarkedNullable) PRIMITIVE_KTYPES.inverse[this] else this
    } ?: error("Only primitive types supported")

public fun KType.primitiveDefault(
    booleanDefault: Boolean = BOOLEAN_DEFAULT,
    uByteDefault: UByte = U_BYTE_DEFAULT,
    uShortDefault: UShort = U_SHORT_DEFAULT,
    uIntDefault: UInt = U_INT_DEFAULT,
    uLongDefault: ULong = U_LONG_DEFAULT,
    byteDefault: Byte = BYTE_DEFAULT,
    shortDefault: Short = SHORT_DEFAULT,
    intDefault: Int = INT_DEFAULT,
    longDefault: Long = LONG_DEFAULT,
    floatDefault: Float = FLOAT_DEFAULT,
    doubleDefault: Double = DOUBLE_DEFAULT,
    charDefault: Char = CHAR_DEFAULT,
    stringDefault: String = STRING_DEFAULT,
    bigIntegerDefault: BigInteger = BIG_INTEGER_DEFAULT,
    bigDecimalDefault: BigDecimal = BIG_DECIMAL_DEFAULT,
    localTimeDefault: LocalTime = LOCAL_TIME_DEFAULT,
    localDateDefault: LocalDate = LOCAL_DATE_DEFAULT,
    localDateTimeDefault: LocalDateTime = LOCAL_DATE_TIME_DEFAULT,
    durationDefault: Duration = DURATION_DEFAULT,
    datePeriodDefault: DatePeriod = DATE_PERIOD_DEFAULT,
    dateTimePeriodDefault: DateTimePeriod = DATE_TIME_PERIOD_DEFAULT,
    uuidDefault: () -> Uuid = { UUID_DEFAULT },
    nullIfNullable: Boolean = true,
): Any? = if (isMarkedNullable && nullIfNullable) null
else kClass.primitiveDefault(
    booleanDefault,
    uByteDefault,
    uShortDefault,
    uIntDefault,
    uLongDefault,
    byteDefault,
    shortDefault,
    intDefault,
    longDefault,
    floatDefault,
    doubleDefault,
    charDefault,
    stringDefault,
    bigIntegerDefault,
    bigDecimalDefault,
    localTimeDefault,
    localDateDefault,
    localDateTimeDefault,
    durationDefault,
    datePeriodDefault,
    dateTimePeriodDefault,
    uuidDefault,
)

public fun KType.classifierOrUpperBound(): KClass<*>? {
    return when (val c = classifier) {
        is KClass<*> -> c
        is KTypeParameter -> c.upperBounds.firstOrNull()?.classifierOrUpperBound()
        else -> null
    }
}
