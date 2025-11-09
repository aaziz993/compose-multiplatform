package klib.data.type.primitives.string.humanreadable

import kotlin.math.roundToInt
import kotlin.time.Duration

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
public inline fun Duration.toHumanReadable(
    nanoseconds: (quantity: Int) -> String = { quantity -> "nanosecond${if (quantity > 1) "s" else ""}" },
    microseconds: (quantity: Int) -> String = { quantity -> "microsecond${if (quantity > 1) "s" else ""}" },
    milliseconds: (quantity: Int) -> String = { quantity -> "millisecond${if (quantity > 1) "s" else ""}" },
    seconds: (quantity: Int) -> String = { quantity -> "second${if (quantity > 1) "s" else ""}" },
    minutes: (quantity: Int) -> String = { quantity -> "minute${if (quantity > 1) "s" else ""}" },
    hours: (quantity: Int) -> String = { quantity -> "hour${if (quantity > 1) "s" else ""}" },
    days: (quantity: Int) -> String = { quantity -> "day${if (quantity > 1) "s" else ""}" },
    weeks: (quantity: Int) -> String = { quantity -> "week${if (quantity > 1) "s" else ""}" },
    months: (quantity: Int) -> String = { quantity -> "month${if (quantity > 1) "s" else ""}" },
    years: (quantity: Int) -> String = { quantity -> "year${if (quantity > 1) "s" else ""}" },
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
        nanosecondsAgo < 1000 -> "$nanosecondsAgo ${nanoseconds(nanosecondsAgo)}"

        microsecondsAgo < 1000 -> "$microsecondsAgo ${microseconds(microsecondsAgo)}"

        millisecondsAgo < 1000 -> "$millisecondsAgo ${milliseconds(millisecondsAgo)}"

        secondsAgo < 60 -> "$secondsAgo ${seconds(secondsAgo)}"

        secondsAgo < 3600 -> "$minutesAgo ${minutes(minutesAgo)}"

        daysAgo < 1 -> "$hoursAgo ${hours(hoursAgo)}"

        daysAgo < 7 -> "$daysAgo ${days(daysAgo)}"

        daysAgo < 30 -> "$weeksAgo ${weeks(weeksAgo)}"

        monthsAgo < 12 || yearsAgo == 0 -> "$monthsAgo ${months(monthsAgo)}"

        else -> "$yearsAgo ${years(yearsAgo)}"
    }
}
