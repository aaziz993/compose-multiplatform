package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.humanreadable.toRelativeHumanReadable
import compose_app.generated.resources.Res
import compose_app.generated.resources.relative_days
import compose_app.generated.resources.relative_hours
import compose_app.generated.resources.relative_microseconds
import compose_app.generated.resources.relative_milliseconds
import compose_app.generated.resources.relative_minutes
import compose_app.generated.resources.relative_months
import compose_app.generated.resources.relative_nanoseconds
import compose_app.generated.resources.relative_seconds
import compose_app.generated.resources.time_in
import compose_app.generated.resources.time_ago
import compose_app.generated.resources.now
import compose_app.generated.resources.relative_weeks
import compose_app.generated.resources.relative_years
import kotlin.time.Duration

/**
 * Returns the duration difference, in human-readable format.
 *
 * @return a formatted string
 */
@Suppress("ComposeUnstableReceiver")
@Composable
public fun Duration.toRelativeHumanReadable(): String = toRelativeHumanReadable(
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
    Res.string.time_in,
    Res.string.now,
    Res.string.time_ago,
)

