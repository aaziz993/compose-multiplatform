package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import klib.data.type.primitives.string.humanreadable.toHumanReadable
import kotlin.time.Instant
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Returns the given duration in human-readable format.
 * For example: a duration of 3 seconds returns "3 seconds".
 *
 * @return a formatted string
 */
@Suppress("ComposeUnstableReceiver")
@Composable
public fun Instant.toHumanReadable(
    baseInstant: Instant,
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
    timeInFuture: StringResource,
    now: StringResource,
    timeAgo: StringResource,
): String = toHumanReadable(
    baseInstant,
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
    stringResource(timeInFuture),
    stringResource(now),
    stringResource(timeAgo),
)

