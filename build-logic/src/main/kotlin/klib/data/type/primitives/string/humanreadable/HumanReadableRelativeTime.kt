package klib.data.type.primitives.string.humanreadable

import klib.data.type.primitives.string.humanreadable.model.RelativeTime
import klib.data.type.primitives.string.humanreadable.model.TimeUnit
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

/**
 * Returns the difference between now and instant, in human-readable format. Also supports
 * instants in the future. For example: an instant that's 5 hours ago will return "5 hours ago".
 *
 * @return a formatted string
 */
@ExperimentalTime
public fun Instant.toHumanReadable(
    baseInstant: Instant,
    nanosecondsUnit: TimeUnit = TimeUnit(
        { "" },
        { "nanosecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    microsecondsUnit: TimeUnit = TimeUnit(
        { "" },
        { "microsecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    millisecondsUnit: TimeUnit = TimeUnit(
        { "" },
        { "millisecond${if (it > 1) "s" else ""}" },
        { "" },
    ),
    secondsUnit: TimeUnit = TimeUnit(
        { "" },
        { "second${if (it > 1) "s" else ""}" },
        { "" },
    ),
    minutesUnit: TimeUnit = TimeUnit(
        { "" },
        { "minute${if (it > 1) "s" else ""}" },
        { "" },
    ),
    hoursUnit: TimeUnit = TimeUnit(
        { "" },
        { "hour${if (it > 1) "s" else ""}" },
        { "" },
    ),
    daysUnit: TimeUnit = TimeUnit(
        { "" },
        { "day${if (it > 1) "s" else ""}" },
        { "" },
    ),
    weeksUnit: TimeUnit = TimeUnit(
        { "" },
        { "week${if (it > 1) "s" else ""}" },
        { "" },
    ),
    monthsUnit: TimeUnit = TimeUnit(
        { "" },
        { "month${if (it > 1) "s" else ""}" },
        { "" },
    ),
    yearsUnit: TimeUnit = TimeUnit(
        { "" },
        { "year${if (it > 1) "s" else ""}" },
        { "" },
    ),
    timeInFuture: String = $$"in ${time}",
    now: String = "now",
    timeAgo: String = $$"${time} ago",
): String {
    val diff = baseInstant - this
    val secondsAgo = diff.inWholeSeconds

    return when {
        secondsAgo < 0 -> timeInFuture.format(
            diff.absoluteValue.toHumanReadable(
                nanosecondsUnit,
                microsecondsUnit,
                millisecondsUnit,
                secondsUnit,
                minutesUnit,
                hoursUnit,
                daysUnit,
                weeksUnit,
                monthsUnit,
                yearsUnit,
                RelativeTime.Future,
            ),
        )

        secondsAgo <= 1 -> now

        else -> timeAgo.format(
            diff.absoluteValue.toHumanReadable(
                nanosecondsUnit,
                microsecondsUnit,
                millisecondsUnit,
                secondsUnit,
                minutesUnit,
                hoursUnit,
                daysUnit,
                weeksUnit,
                monthsUnit,
                yearsUnit,
                RelativeTime.Past,
            ),
        )
    }
}
