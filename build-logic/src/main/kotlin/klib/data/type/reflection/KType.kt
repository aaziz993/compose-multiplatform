@file:OptIn(ExperimentalUuidApi::class)
package klib.data.type.reflection

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.BIG_DECIMAL_DEFAULT
import klib.data.type.primitives.BIG_INTEGER_DEFAULT
import klib.data.type.primitives.BOOLEAN_DEFAULT
import klib.data.type.primitives.BYTE_DEFAULT
import klib.data.type.primitives.CHAR_DEFAULT
import klib.data.type.primitives.DATE_PERIOD_DEFAULT
import klib.data.type.primitives.DATE_TIME_PERIOD_DEFAULT
import klib.data.type.primitives.DOUBLE_DEFAULT
import klib.data.type.primitives.DURATION_DEFAULT
import klib.data.type.primitives.FLOAT_DEFAULT
import klib.data.type.primitives.INT_DEFAULT
import klib.data.type.primitives.LOCAL_DATE_DEFAULT
import klib.data.type.primitives.LOCAL_DATE_TIME_DEFAULT
import klib.data.type.primitives.LOCAL_TIME_DEFAULT
import klib.data.type.primitives.LONG_DEFAULT
import klib.data.type.primitives.SHORT_DEFAULT
import klib.data.type.primitives.STRING_DEFAULT
import klib.data.type.primitives.UUID_DEFAULT
import klib.data.type.primitives.U_BYTE_DEFAULT
import klib.data.type.primitives.U_INT_DEFAULT
import klib.data.type.primitives.U_LONG_DEFAULT
import klib.data.type.primitives.U_SHORT_DEFAULT
import klib.data.type.reflection.primitiveDefault
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public val KType.kClass: KClass<*>
    get() = classifier as KClass<*>

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