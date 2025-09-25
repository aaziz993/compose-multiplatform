package klib.data.type.primitives

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

public val LOCAL_TIME_DEFAULT: LocalTime = LocalTime(0, 0)

public val LOCAL_DATE_DEFAULT: LocalDate = LocalDate(0, 0, 0)

public val LOCAL_DATE_TIME_DEFAULT: LocalDateTime = LocalDateTime(0, 0, 0, 0, 0)

public val DURATION_DEFAULT: Duration = 0.seconds

public val DATE_PERIOD_DEFAULT: DatePeriod = DatePeriod()

public val DATE_TIME_PERIOD_DEFAULT: DateTimePeriod = DateTimePeriod()


public val nowInstant: Instant
    get() = Clock.System.now()

public val nowEpochMillis: Long
    get() = nowInstant.toEpochMilliseconds()

public val zeroTime: LocalTime = LocalTime.fromSecondOfDay(0)

public fun LocalTime.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalTime = LocalDateTime.now(zone).time

public fun LocalTime.Companion.parseOrNull(s: String): LocalTime? = s.runCatching { parse(this) }.getOrNull()

public val zeroDate: LocalDate = LocalDate.fromEpochDays(0)

public fun LocalDate.toInstant(timeZone: TimeZone): Instant = atStartOfDayIn(timeZone)

public fun LocalDate.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = LocalDateTime.now(zone).date

public fun LocalDate.Companion.parseOrNull(s: String): LocalDate? = s.runCatching { parse(this) }.getOrNull()

public fun LocalDate.toEpochMilliseconds(): Long = toEpochDays() * 86400000L

public val zeroDateTime: LocalDateTime = LocalDateTime(zeroDate, zeroTime)

public fun LocalDateTime.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    Clock.System.now().toLocalDateTime(zone)

public fun LocalDateTime.Companion.parseOrNull(s: String): LocalDateTime? = s.runCatching { parse(this) }.getOrNull()

public fun DatePeriod.Companion.parseOrNull(s: String): DatePeriod? = s.runCatching { parse(this) }.getOrNull()

public fun DateTimePeriod.Companion.parseOrNull(s: String): DateTimePeriod? = s.runCatching { parse(this) }.getOrNull()
