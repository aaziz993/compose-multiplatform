package klib.data.type.primitives.string.humanreadable

import klib.data.type.primitives.string.format
import kotlin.time.Instant

/**
 * Returns the difference between now and instant, in human-readable format. Also supports
 * instants in the future. For example: an instant that's 5 hours ago will return "5 hours ago".
 *
 * @return a formatted string
 */
public inline fun Instant.toHumanReadable(
    baseInstant: Instant,
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
    val diff = baseInstant - this
    val secondsAgo = diff.inWholeSeconds

    return when {
        secondsAgo < 0 -> timeInFuture.format(
            diff.absoluteValue.toHumanReadable(
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
            diff.absoluteValue.toHumanReadable(
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
