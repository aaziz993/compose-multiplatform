package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import klib.data.type.primitives.time.toLocalTime
import kotlin.time.Clock
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import pro.respawn.kmmutils.datetime.plusDays
import pro.respawn.kmmutils.datetime.withTime

@Composable
public fun isAdaptiveDark(
    dayStart: LocalTime = LocalTime(6, 0),
    nightStart: LocalTime = LocalTime(19, 0),
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean {
    val isNight by produceState(
        isNight(dayStart, nightStart, clock, timeZone),
        dayStart,
        nightStart,
        timeZone,
    ) {
        while (true) {
            val now = clock.now().toLocalDateTime(timeZone)

            val nextChange = when {
                now.time < dayStart -> now.withTime(dayStart)
                now.time < nightStart -> now.withTime(nightStart)
                else -> now.plusDays(1, timeZone)
                    .withTime(dayStart)
            }

            val delayMs = nextChange.toInstant(timeZone)
                .minus(clock.now())
                .inWholeMilliseconds
                .coerceAtLeast(0)

            delay(delayMs)
            value = isNight(dayStart, nightStart, clock, timeZone)
        }
    }

    return isNight
}

private fun isNight(
    dayStart: LocalTime,
    nightStart: LocalTime,
    clock: Clock,
    timeZone: TimeZone,
): Boolean {
    val now = clock.now().toLocalTime(timeZone)
    return now < dayStart || now >= nightStart
}
