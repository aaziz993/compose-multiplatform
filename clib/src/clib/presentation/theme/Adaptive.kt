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
    lightTime: LocalTime = LocalTime(6, 0),
    darkTime: LocalTime = LocalTime(19, 0),
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Boolean {
    val isNight by produceState(
        isDarkTime(darkTime, clock, timeZone),
        lightTime,
        darkTime,
        timeZone,
    ) {
        while (true) {
            val now = clock.now().toLocalDateTime(timeZone)

            val nextChange = when {
                now.time < lightTime -> now.withTime(lightTime)
                now.time < darkTime -> now.withTime(darkTime)
                else -> now.plusDays(1, timeZone)
                    .withTime(lightTime)
            }

            val delayMs = nextChange.toInstant(timeZone)
                .minus(clock.now())
                .inWholeMilliseconds
                .coerceAtLeast(0)

            delay(delayMs)
            value = isDarkTime(darkTime, clock, timeZone)
        }
    }

    return isNight
}

private fun isDarkTime(
    darkTime: LocalTime,
    clock: Clock,
    timeZone: TimeZone,
): Boolean = clock.now().toLocalTime(timeZone) >= darkTime
