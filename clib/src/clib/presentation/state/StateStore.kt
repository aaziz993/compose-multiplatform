package clib.presentation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor

@Suppress("ComposeCompositionLocalUsage")
public val LocalStateStore: ProvidableCompositionLocal<StateStore> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalStateStore") }

/**
 * A store for passing states.
 *
 * It provides a solution for state based value.
 */
public class StateStore(initialValue: Map<String, MutableState<Any?>> = emptyMap()) {

    /**
     * Map from the value key to a mutable state of the value.
     */
    public val stateMap: MutableMap<String, MutableState<Any?>> =
        mutableMapOf<String, MutableState<Any?>>().apply { putAll(initialValue) }

    /**
     * Retrieves the current value of the given key.
     */
    public inline operator fun <reified T> get(key: String = T::class.toString()): T =
        requireNotNull(stateMap[key]) { "No state for key '$key'" }.value as T

    /**
     * Sets the value for the given key.
     */
    public inline operator fun <reified T> set(key: String = T::class.toString(), value: T): Unit =
        if (key in stateMap) stateMap[key]!!.value = value else stateMap[key] = mutableStateOf(value)

    /**
     * Removes all values associated with the given key from the store.
     */
    public inline fun <reified T> remove(key: String = T::class.toString()) {
        stateMap.remove(key)
    }

    public companion object {

        /** Saver to save and restore the StateStore across config change and process death. */
        public val Saver: Saver<StateStore, *> =
            Saver(
                save = { it.stateMap },
                restore = { StateStore(it) },
            )
    }
}

/**
 * Provides a [StateStore] that will be remembered across configuration changes.
 */
@Composable
public fun rememberStateStore(initialValue: Map<String, MutableState<Any?>> = emptyMap()): StateStore =
    rememberSaveable(saver = StateStore.Saver) { StateStore(initialValue) }
