package klib.data.type.primitives.string.humanreadable

import klib.data.type.primitives.string.humanreadable.model.RelativeTime
import klib.data.type.primitives.string.humanreadable.model.TimeUnit
import kotlin.math.roundToInt
import kotlin.time.Duration

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
public fun Duration.toHumanReadable(): String = toHumanReadable(
    TimeUnit(
        { "" },
        { "nanosecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "microsecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "millisecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "second${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "minute${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "hour${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "day${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "week${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "month${if (it > 1) "s" else ""}" },
        { "" },
    ),
    TimeUnit(
        { "" },
        { "year${if (it > 1) "s" else ""}" },
        { "" },
    ),
    RelativeTime.Present,
)

internal fun Duration.toHumanReadable(
    nanosecondsUnit: TimeUnit,
    microsecondsUnit: TimeUnit,
    millisecondsUnit: TimeUnit,
    secondsUnit: TimeUnit,
    minutesUnit: TimeUnit,
    hoursUnit: TimeUnit,
    daysUnit: TimeUnit,
    weeksUnit: TimeUnit,
    monthsUnit: TimeUnit,
    yearsUnit: TimeUnit,
    relativeTime: RelativeTime
): String {
    val nanosecondsAgo = inWholeMicroseconds.toInt()
    val microsecondsAgo = inWholeMicroseconds.toInt()
    val millisecondsAgo = inWholeMicroseconds.toInt()
    val secondsAgo = inWholeSeconds.toInt()
    val minutesAgo = inWholeMinutes.toInt()
    val hoursAgo = inWholeHours.toInt()
    val daysAgo = inWholeDays.toInt()
    val weeksAgo = (inWholeDays / 7f).roundToInt()
    val monthsAgo = (inWholeDays / 30.5f).roundToInt()
    val yearsAgo = (inWholeDays / 365).toInt()

    return when {
        nanosecondsAgo < 1000 -> "$nanosecondsAgo ${nanosecondsUnit.format(nanosecondsAgo, relativeTime)}"

        microsecondsAgo < 1000 -> "$microsecondsAgo ${microsecondsUnit.format(microsecondsAgo, relativeTime)}"

        millisecondsAgo < 1000 -> "$millisecondsAgo ${millisecondsUnit.format(millisecondsAgo, relativeTime)}"

        secondsAgo < 60 -> "$secondsAgo ${secondsUnit.format(secondsAgo, relativeTime)}"

        secondsAgo < 3600 -> "$minutesAgo ${minutesUnit.format(minutesAgo, relativeTime)}"

        daysAgo < 1 -> "$hoursAgo ${hoursUnit.format(hoursAgo, relativeTime)}"

        daysAgo < 7 -> "$daysAgo ${daysUnit.format(daysAgo, relativeTime)}"

        daysAgo < 30 -> "$weeksAgo ${weeksUnit.format(weeksAgo, relativeTime)}"

        monthsAgo < 12 || yearsAgo == 0 -> "$monthsAgo ${monthsUnit.format(monthsAgo, relativeTime)}"

        else -> "$yearsAgo ${yearsUnit.format(yearsAgo, relativeTime)}"
    }
}
