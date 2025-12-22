package klib.data.type.primitives.string.humanreadable

import klib.data.type.primitives.string.formatter.format
import kotlin.time.Duration

/**
 * Returns the duration difference, in human-readable format.
 *
 * @return a formatted string
 */
public inline fun Duration.toRelativeHumanReadable(
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
    timeInFuture: String = $$"in ${time}",
    now: String = "now",
    timeAgo: String = $$"${time} ago",
): String {
    val secondsAgo = inWholeSeconds

    return when {
        secondsAgo < 0 -> timeInFuture.format(
            absoluteValue.toHumanReadable(
                nanoseconds,
                microseconds,
                milliseconds,
                seconds,
                minutes,
                hours,
                days,
                weeks,
                months,
                years,
            ),
        )

        secondsAgo <= 1 -> now

        else -> timeAgo.format(
            absoluteValue.toHumanReadable(
                nanoseconds,
                microseconds,
                milliseconds,
                seconds,
                minutes,
                hours,
                days,
                weeks,
                months,
                years,
            ),
        )
    }
}
