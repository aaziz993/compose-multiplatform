package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import klib.data.type.primitives.string.humanreadable.toHumanReadable
import kotlin.time.Duration
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.pluralStringResource

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
@Composable
public fun Duration.toHumanReadable(
    nanoseconds: PluralStringResource,
    microseconds: PluralStringResource,
    milliseconds: PluralStringResource,
    seconds: PluralStringResource,
    minutes: PluralStringResource,
    hours: PluralStringResource,
    days: PluralStringResource,
    weeks: PluralStringResource,
    months: PluralStringResource,
    years: PluralStringResource,
): String = toHumanReadable(
    { quantity -> pluralStringResource(nanoseconds, quantity) },
    { quantity -> pluralStringResource(microseconds, quantity) },
    { quantity -> pluralStringResource(milliseconds, quantity) },
    { quantity -> pluralStringResource(seconds, quantity) },
    { quantity -> pluralStringResource(minutes, quantity) },
    { quantity -> pluralStringResource(hours, quantity) },
    { quantity -> pluralStringResource(days, quantity) },
    { quantity -> pluralStringResource(weeks, quantity) },
    { quantity -> pluralStringResource(months, quantity) },
    { quantity -> pluralStringResource(years, quantity) },
)
