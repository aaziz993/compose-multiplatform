package clib.presentation.navigation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import clib.presentation.events.EventBus

/**
 * Local for receiving results in a [EventBus]
 */
public object LocalResultEventBus {

    @Suppress("ComposeCompositionLocalUsage")
    private val LocalResultEventBus: ProvidableCompositionLocal<EventBus?> =
        compositionLocalOf { null }

    /**
     * The current [EventBus]
     */
    public val current: EventBus
        @Composable
        get() = LocalResultEventBus.current ?: error("No ResultEventBus has been provided")

    /**
     * Provides a [EventBus] to the composition
     */
    public infix fun provides(
        bus: EventBus
    ): ProvidedValue<EventBus?> = LocalResultEventBus.provides(bus)
}

