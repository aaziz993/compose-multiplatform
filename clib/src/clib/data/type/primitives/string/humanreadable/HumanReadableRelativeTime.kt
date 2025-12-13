package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.pluralStringResource
import klib.data.type.primitives.string.humanreadable.toRelativeHumanReadable
import kotlin.time.Duration
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import clib.data.type.primitives.string.stringResource

/**
 * Returns the duration difference, in human-readable format.
 *
 * @return a formatted string
 */
@Suppress("ComposeUnstableReceiver")
@Composable
public fun Duration.toRelativeHumanReadable(
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
): String = toRelativeHumanReadable(
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

