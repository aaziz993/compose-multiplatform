package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.config.RouteConfig

@Suppress("ComposeCompositionLocalUsage")
public val LocalRoutesState: ProvidableCompositionLocal<RoutesState> =
    staticCompositionLocalOf(::RoutesState)

public class RoutesState(initialValue: Map<String, RouteConfig> = emptyMap()) {

    public var value: Map<String, RouteConfig> by mutableStateOf(initialValue)

    public operator fun get(route: BaseRoute): RouteConfig? = value[route.name]

    public companion object {

        /** Saver to save and restore the RoutesState across config change and process death. */
        public val Saver: Saver<RoutesState, *> =
            Saver(
                save = { it.value },
                restore = { RoutesState(it) },
            )
    }
}

@Composable
public fun rememberRoutesState(initialValue: Map<String, RouteConfig> = emptyMap()): RoutesState =
    rememberSaveable(saver = RoutesState.Saver) { RoutesState(initialValue) }
