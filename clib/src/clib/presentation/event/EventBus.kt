package clib.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Suppress("ComposeCompositionLocalUsage")
public val LocalEventBus: ProvidableCompositionLocal<EventBus> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalEventBus") }

/**
 * An EventBus for passing value.
 *
 * It provides a solution for event based value.
 */
public class EventBus {

    /**
     * Map from the value key to a channel of values.
     */
    public val channelMap: MutableMap<String, Channel<Any?>> = mutableMapOf()

    /**
     * Provides a flow for the given key.
     */
    public inline operator fun <reified T> get(key: String = T::class.toString()): Flow<Any?>? =
        channelMap[key]?.receiveAsFlow()

    /**
     * Sends a value into the channel associated with the given key.
     */
    public inline fun <reified T> set(key: String = T::class.toString(), value: T) {
        if (key !in channelMap)
            channelMap[key] = Channel(capacity = BUFFERED, onBufferOverflow = BufferOverflow.SUSPEND)

        channelMap[key]?.trySend(value)
    }

    /**
     * Removes all values associated with the given key from the store.
     */
    public inline fun <reified T> removeResult(key: String = T::class.toString()) {
        channelMap.remove(key)
    }
}

/**
 * An Effect to provide a value event.
 *
 * The trailing lambda provides the value from a flow of value.
 *
 * @param eventBus the ResultEventBus to retrieve the value from. The default value
 * is read from the `LocalResultEventBus` composition local.
 * @param key the key that should be associated with this effect.
 * @param onValue the callback to invoke when a value is received.
 */
@Composable
public inline fun <reified T> EventEffect(
    eventBus: EventBus = LocalEventBus.current,
    key: String = T::class.toString(),
    crossinline onValue: suspend (T) -> Unit
): Unit = LaunchedEffect(key, eventBus.channelMap[key]) {
    eventBus.get<T>(key)?.collect { value ->
        onValue(value as T)
    }
}

