package klib.data.type.reflection

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.number.BIG_DECIMAL_DEFAULT
import klib.data.type.primitives.number.BIG_INTEGER_DEFAULT
import klib.data.type.primitives.BOOLEAN_DEFAULT
import klib.data.type.primitives.number.BYTE_DEFAULT
import klib.data.type.primitives.number.CHAR_DEFAULT
import klib.data.type.primitives.time.DATE_PERIOD_DEFAULT
import klib.data.type.primitives.time.DATE_TIME_PERIOD_DEFAULT
import klib.data.type.primitives.number.DOUBLE_DEFAULT
import klib.data.type.primitives.time.DURATION_DEFAULT
import klib.data.type.primitives.number.FLOAT_DEFAULT
import klib.data.type.primitives.number.INT_DEFAULT
import klib.data.type.primitives.time.LOCAL_DATE_DEFAULT
import klib.data.type.primitives.time.LOCAL_DATE_TIME_DEFAULT
import klib.data.type.primitives.time.LOCAL_TIME_DEFAULT
import klib.data.type.primitives.number.LONG_DEFAULT
import klib.data.type.primitives.number.SHORT_DEFAULT
import klib.data.type.primitives.number.STRING_DEFAULT
import klib.data.type.primitives.UUID_DEFAULT
import klib.data.type.primitives.number.U_BYTE_DEFAULT
import klib.data.type.primitives.number.U_INT_DEFAULT
import klib.data.type.primitives.number.U_LONG_DEFAULT
import klib.data.type.primitives.number.U_SHORT_DEFAULT
import klib.data.type.primitives.number.parseOrNull
import klib.data.type.primitives.parseOrNull
import klib.data.type.primitives.time.parseOrNull
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.uuid.Uuid
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat

//////////////////////////////////////////////////////////GENERIC///////////////////////////////////////////////////////
public val KClass<*>.isUIntNumber: Boolean
    get() = when (this) {
        UByte::class, UShort::class, UInt::class, ULong::class -> true
        else -> false
    }

public val KClass<*>.isIntNumber: Boolean
    get() = when (this) {
        Byte::class, Short::class, Int::class, Long::class -> true
        else -> false
    }

public val KClass<*>.isFloatNumber: Boolean
    get() = when (this) {
        Float::class, Double::class -> true
        else -> false
    }

public val KClass<*>.isBigNumber: Boolean
    get() = when (this) {
        BigInteger::class, BigDecimal::class -> true
        else -> false
    }

public val KClass<*>.isNumber: Boolean
    get() = isUIntNumber || isIntNumber || isFloatNumber || isBigNumber

public val KClass<*>.isChar: Boolean
    get() = this == Char::class

public val KClass<*>.isString: Boolean
    get() = this == String::class

public val KClass<*>.isSymbolic: Boolean
    get() = isChar || isString

public val KClass<*>.isTime: Boolean
    get() = when (this) {
        LocalTime::class, LocalDate::class, LocalDateTime::class, Duration::class, DatePeriod::class, DateTimePeriod::class -> true
        else -> false
    }

public val KClass<*>.isPrimitive: Boolean
    get() = isNumber || isSymbolic || isTime || this == Uuid::class

public val KClass<*>.isList: Boolean
    get() = when (this) {
        List::class, MutableList::class, ArrayList::class -> true
        else -> false
    }

public val KClass<*>.isSet: Boolean
    get() = when (this) {
        Set::class, MutableSet::class, HashSet::class, LinkedHashSet::class -> true
        else -> false
    }

public val KClass<*>.isMap: Boolean
    get() = when (this) {
        Map::class, MutableMap::class, HashMap::class, LinkedHashMap::class -> true
        else -> false
    }

public fun KClass<*>.primitiveDefault(
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
    uuidDefault: () -> Uuid = { UUID_DEFAULT }
): Any =
    when (this) {
        Boolean::class -> booleanDefault

        UByte::class -> uByteDefault

        UShort::class -> uShortDefault

        UInt::class -> uIntDefault

        ULong::class -> uLongDefault

        Byte::class -> byteDefault

        Short::class -> shortDefault

        Int::class -> intDefault

        Long::class -> longDefault

        Float::class -> floatDefault

        Double::class -> doubleDefault

        Char::class -> charDefault

        String::class -> stringDefault

        BigInteger::class -> bigIntegerDefault

        BigDecimal::class -> bigDecimalDefault

        LocalTime::class -> localTimeDefault

        LocalDate::class -> localDateDefault

        LocalDateTime::class -> localDateTimeDefault

        Duration::class -> durationDefault

        DatePeriod::class -> datePeriodDefault

        DateTimePeriod::class -> dateTimePeriodDefault

        Uuid::class -> uuidDefault()

        else -> throw IllegalArgumentException("Unknown type \"$simpleName\"")
    }

public fun KClass<*>.parsePrimitiveOrNull(
    value: String,
    dateTimeFormat: DateTimeFormat<DateTimeComponents>? = null,
): Any? =
    when (this) {
        Boolean::class -> value.toBooleanStrictOrNull()

        UByte::class -> value.toUByteOrNull()

        UShort::class -> value.toUShortOrNull()

        UInt::class -> value.toUIntOrNull()

        ULong::class -> value.toULongOrNull()

        Byte::class -> value.toByteOrNull()

        Short::class -> value.toShortOrNull()

        Int::class -> value.toIntOrNull()

        Long::class -> value.toLongOrNull()

        Float::class -> value.toFloatOrNull()

        Double::class -> value.toDoubleOrNull()

        Char::class -> value[0]

        String::class -> value

        BigInteger::class -> BigInteger.parseOrNull(value)

        BigDecimal::class -> BigDecimal.parseOrNull(value)

        LocalTime::class -> dateTimeFormat?.parseOrNull(value)?.toLocalTime()
            ?: LocalTime.parseOrNull(value)

        LocalDate::class -> dateTimeFormat?.parseOrNull(value)?.toLocalDate()
            ?: LocalDate.parseOrNull(value)

        LocalDateTime::class -> dateTimeFormat?.parseOrNull(value)?.toLocalDateTime()
            ?: LocalDateTime.parseOrNull(value)

        Duration::class -> Duration.parseOrNull(value)

        DatePeriod::class -> DatePeriod.parseOrNull(value)

        DateTimePeriod::class -> DateTimePeriod.parseOrNull(value)

        Uuid::class -> Uuid.parseOrNull(value)

        else -> null
    }

public fun KClass<*>.parsePrimitive(
    value: String,
    dateTimeFormat: DateTimeFormat<DateTimeComponents>? = null,
): Any = parsePrimitiveOrNull(value, dateTimeFormat)
    ?: throw IllegalArgumentException("Unknown type '$simpleName'")
