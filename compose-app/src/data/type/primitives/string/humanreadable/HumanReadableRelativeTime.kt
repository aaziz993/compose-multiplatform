package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.humanreadable.toHumanReadable
import compose_app.generated.resources.Res
import compose_app.generated.resources.days
import compose_app.generated.resources.hours
import compose_app.generated.resources.microseconds
import compose_app.generated.resources.milliseconds
import compose_app.generated.resources.minutes
import compose_app.generated.resources.months
import compose_app.generated.resources.nanoseconds
import compose_app.generated.resources.seconds
import compose_app.generated.resources.time_ago
import compose_app.generated.resources.now
import compose_app.generated.resources.weeks
import compose_app.generated.resources.years
import kotlin.time.Instant

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
@Suppress("ComposeUnstableReceiver")
@Composable
public fun Instant.toHumanReadable(baseInstant: Instant): String = toHumanReadable(
    baseInstant,
    Res.plurals.nanoseconds,
    Res.plurals.microseconds,
    Res.plurals.milliseconds,
    Res.plurals.seconds,
    Res.plurals.minutes,
    Res.plurals.hours,
    Res.plurals.days,
    Res.plurals.weeks,
    Res.plurals.months,
    Res.plurals.years,
    Res.string.now,
    Res.string.now,
    Res.string.time_ago,
)

