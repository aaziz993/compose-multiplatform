package clib.presentation.navigation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import clib.presentation.state.StateStore

/**
 * Local for storing results in a [StateStore]
 */
public object LocalResultStore {

    @Suppress("ComposeCompositionLocalUsage")
    private val LocalResultStore: ProvidableCompositionLocal<StateStore?> =
        compositionLocalOf { null }

    /**
     * The current [StateStore]
     */
    public val current: StateStore
        @Composable
        get() = LocalResultStore.current ?: error("No ResultStore has been provided")

    /**
     * Provides a [StateStore] to the composition
     */
    public infix fun provides(
        store: StateStore
    ): ProvidedValue<StateStore?> = LocalResultStore.provides(store)
}




