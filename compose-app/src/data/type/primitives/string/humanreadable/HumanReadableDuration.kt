package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.humanreadable.toHumanReadable
import compose_app.generated.resources.Res
import compose_app.generated.resources.relative_days
import compose_app.generated.resources.relative_hours
import compose_app.generated.resources.relative_microseconds
import compose_app.generated.resources.relative_milliseconds
import compose_app.generated.resources.relative_minutes
import compose_app.generated.resources.relative_months
import compose_app.generated.resources.relative_nanoseconds
import compose_app.generated.resources.relative_seconds
import compose_app.generated.resources.relative_weeks
import compose_app.generated.resources.relative_years
import kotlin.time.Duration

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
@Composable
public fun Duration.toHumanReadable(): String = toHumanReadable(
    Res.plurals.relative_nanoseconds,
    Res.plurals.relative_microseconds,
    Res.plurals.relative_milliseconds,
    Res.plurals.relative_seconds,
    Res.plurals.relative_minutes,
    Res.plurals.relative_hours,
    Res.plurals.relative_days,
    Res.plurals.relative_weeks,
    Res.plurals.relative_months,
    Res.plurals.relative_years,
)
