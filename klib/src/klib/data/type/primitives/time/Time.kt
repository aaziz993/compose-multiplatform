package klib.data.type.primitives.time

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

public val LOCAL_TIME_DEFAULT: LocalTime = LocalTime(0, 0)

public val LOCAL_DATE_DEFAULT: LocalDate = LocalDate(0, 1, 1)

public val LOCAL_DATE_TIME_DEFAULT: LocalDateTime = LocalDateTime(0, 1, 1, 0, 0)

public val DURATION_DEFAULT: Duration = 0.seconds

public val DATE_PERIOD_DEFAULT: DatePeriod = DatePeriod()

public val DATE_TIME_PERIOD_DEFAULT: DateTimePeriod = DateTimePeriod()

public val nowInstant: Instant
    get() = Clock.System.now()

public val nowEpochMillis: Long
    get() = nowInstant.toEpochMilliseconds()

public val ZERO_TIME: LocalTime = LocalTime.fromSecondOfDay(0)

public fun String.toDuration(): Duration = Duration.parse(this)

public fun String.toDurationOrNull(): Duration? = Duration.parseOrNull(this)

public fun String.toDatePeriod(): DatePeriod = DatePeriod.parse(this)

public fun DatePeriod.Companion.parseOrNull(s: String): DatePeriod? =
    s.runCatching { parse(this) }.getOrNull()

public fun DateTimePeriod.Companion.parseOrNull(s: String): DateTimePeriod? =
    s.runCatching { parse(this) }.getOrNull()

public fun String.toDateTimePeriod(): DateTimePeriod = DateTimePeriod.parse(this)

public fun String.toDateTimePeriodOrNull(): DateTimePeriod? = DateTimePeriod.parseOrNull(this)

public fun String.toInstant(): Instant = Instant.parse(this)

public fun String.toInstantOrNull(): Instant? = Instant.parseOrNull(this)

public fun Instant.toLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate = toLocalDateTime(timeZone).date

public fun Any.toInstant(timeZone: TimeZone = TimeZone.UTC): Instant = when (this) {
    is Instant -> this
    is LocalDate -> toInstant(timeZone)
    is LocalDateTime -> toInstant(timeZone)
    else -> throw IllegalArgumentException("Unknown type for toInstant(): ${this::class}")
}

public fun LocalTime.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalTime =
    LocalDateTime.now(zone).time

public fun LocalTime.Companion.parseOrNull(s: String): LocalTime? =
    s.runCatching { parse(this) }.getOrNull()

public fun String.toLocalTime(): LocalTime = LocalTime.parse(this)

public fun String.toLocalTimeOrNull(): LocalTime? = LocalTime.parseOrNull(this)

public fun Any.toLocalTime(timeZone: TimeZone = TimeZone.UTC): LocalTime = when (this) {
    is Instant -> toLocalDateTime(timeZone).time
    is LocalDate -> time
    is LocalDateTime -> time
    else -> throw IllegalArgumentException("Unknown type for toLocalDate(): ${this::class}")
}

public val ZERO_DATE: LocalDate = LocalDate.fromEpochDays(0)

@Suppress("UnusedReceiverParameter")
public val LocalDate.time: LocalTime
    get() = ZERO_TIME

public fun LocalDate.toInstant(timeZone: TimeZone): Instant = atStartOfDayIn(timeZone)

public fun LocalDate.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    LocalDateTime.now(zone).date

public fun LocalDate.Companion.parseOrNull(s: String): LocalDate? =
    s.runCatching { parse(this) }.getOrNull()

public fun String.toLocalDate(): LocalDate = LocalDate.parse(this)

public fun String.toLocalDateOrNull(): LocalDate = LocalDate.parse(this)

public fun LocalDate.toEpochMilliseconds(): Long = toEpochDays() * 86400000L

public fun LocalDate.toLocalDateTime(): LocalDateTime = LocalDateTime(year, month, day, 0, 0)

public fun Any.toLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate = when (this) {
    is Instant -> toLocalDate(timeZone)
    is LocalDate -> this
    is LocalDateTime -> date
    else -> throw IllegalArgumentException("Unknown type for toLocalDate(): ${this::class}")
}

public val ZERO_DATE_TIME: LocalDateTime = LocalDateTime(ZERO_DATE, ZERO_TIME)

public fun LocalDateTime.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    Clock.System.now().toLocalDateTime(zone)

public fun LocalDateTime.Companion.parseOrNull(s: String): LocalDateTime? =
    s.runCatching { parse(this) }.getOrNull()

public fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this)

public fun String.toLocalDateTimeOrNull(): LocalDateTime? = LocalDateTime.parseOrNull(this)

public fun Any.toLocalDateTime(timeZone: TimeZone = TimeZone.UTC): LocalDateTime = when (this) {
    is Instant -> toLocalDateTime(timeZone)
    is LocalDate -> toLocalDateTime()
    is LocalDateTime -> this
    else -> throw IllegalArgumentException("Unknown type for toLocalDateTime(): ${this::class}")
}

public fun createLocalDateTimeFormatter(fraction: Int = 0): DateTimeFormat<LocalDateTime> =
    LocalDateTime.Format {
        date(LocalDate.Formats.ISO)
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
        if (fraction in 1..9) {
            char('.')
            secondFraction(fraction)
        }
    }
