package klib.data.db.exposed

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import klib.data.db.exposed.column.KotlinDecimalColumnType
import klib.data.db.exposed.column.KotlinUuidColumnType
import klib.data.type.collections.bimap.biMapOf
import klib.data.type.primitives.time.now
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.v1.core.BinaryColumnType
import org.jetbrains.exposed.v1.core.BooleanColumnType
import org.jetbrains.exposed.v1.core.ByteColumnType
import org.jetbrains.exposed.v1.core.CharColumnType
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.DecimalColumnType
import org.jetbrains.exposed.v1.core.DoubleColumnType
import org.jetbrains.exposed.v1.core.FloatColumnType
import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.LongColumnType
import org.jetbrains.exposed.v1.core.ShortColumnType
import org.jetbrains.exposed.v1.core.StringColumnType
import org.jetbrains.exposed.v1.core.TextColumnType
import org.jetbrains.exposed.v1.core.UByteColumnType
import org.jetbrains.exposed.v1.core.UIntegerColumnType
import org.jetbrains.exposed.v1.core.ULongColumnType
import org.jetbrains.exposed.v1.core.UShortColumnType
import org.jetbrains.exposed.v1.core.UUIDColumnType
import org.jetbrains.exposed.v1.datetime.KotlinDurationColumnType
import org.jetbrains.exposed.v1.datetime.KotlinInstantColumnType
import org.jetbrains.exposed.v1.datetime.KotlinLocalDateColumnType
import org.jetbrains.exposed.v1.datetime.KotlinLocalDateTimeColumnType
import org.jetbrains.exposed.v1.datetime.KotlinLocalTimeColumnType
import org.jetbrains.exposed.v1.datetime.KotlinOffsetDateTimeColumnType

public val COLUMN_TYPES: Map<KType, KType> = biMapOf(
    typeOf<BooleanColumnType>() to typeOf<Boolean>(),
    typeOf<UByteColumnType>() to typeOf<Byte>(),
    typeOf<UShortColumnType>() to typeOf<Short>(),
    typeOf<UIntegerColumnType>() to typeOf<Int>(),
    typeOf<ULongColumnType>() to typeOf<Long>(),
    typeOf<ByteColumnType>() to typeOf<Byte>(),
    typeOf<ShortColumnType>() to typeOf<Short>(),
    typeOf<IntegerColumnType>() to typeOf<Int>(),
    typeOf<LongColumnType>() to typeOf<Long>(),
    typeOf<FloatColumnType>() to typeOf<Float>(),
    typeOf<DoubleColumnType>() to typeOf<Double>(),
    typeOf<CharColumnType>() to typeOf<Char>(),
    typeOf<StringColumnType>() to typeOf<String>(),
    typeOf<DecimalColumnType>() to typeOf<java.math.BigDecimal>(),
    typeOf<KotlinDecimalColumnType>() to typeOf<BigDecimal>(),
    typeOf<TextColumnType>() to typeOf<String>(),
    typeOf<BinaryColumnType>() to typeOf<ByteArray>(),
    typeOf<UUIDColumnType>() to typeOf<UUID>(),
    typeOf<KotlinUuidColumnType>() to typeOf<Uuid>(),
    typeOf<KotlinOffsetDateTimeColumnType>() to typeOf<OffsetDateTime>(),
    typeOf<KotlinLocalTimeColumnType>() to typeOf<LocalTime>(),
    typeOf<KotlinLocalDateColumnType>() to typeOf<LocalDate>(),
    typeOf<KotlinLocalDateTimeColumnType>() to typeOf<LocalDateTime>(),
    typeOf<KotlinInstantColumnType>() to typeOf<Instant>(),
    typeOf<KotlinDurationColumnType>() to typeOf<Duration>(),
    typeOf<KotlinDurationColumnType>() to typeOf<Duration>(),
)

public fun Column<*>.now(timeZone: TimeZone) = when (columnType) {
    is KotlinLocalTimeColumnType -> LocalTime.now(timeZone)
    is KotlinLocalDateColumnType -> LocalDate.now(timeZone)
    is KotlinLocalDateTimeColumnType -> LocalDateTime.now(timeZone)

    else -> error("Invalid time column: $name")
}
