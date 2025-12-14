package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.config.RouteConfig

@Suppress("ComposeCompositionLocalUsage")
public val LocalRoutesState: ProvidableCompositionLocal<RoutesState> =
    staticCompositionLocalOf(::RoutesState)

public class RoutesState(initialValue: Map<String, RouteConfig> = emptyMap()) {

    public val value: MutableMap<String, RouteConfig> =
        mutableStateMapOf<String, RouteConfig>().apply { putAll(initialValue) }

    public operator fun get(route: String): RouteConfig =
        value.getOrPut(route, ::RouteConfig)

    public operator fun set(route: String, value: RouteConfig) {
        this.value[route] = value
    }

    public operator fun get(route: BaseRoute): RouteConfig = this[route.name]

    public operator fun set(route: BaseRoute, value: RouteConfig) {
        this[route.name] = value
    }

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
