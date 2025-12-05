package clib.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor

@Suppress("ComposeCompositionLocalUsage")
public val LocalComponentsState: ProvidableCompositionLocal<ComponentsState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalComponentsState") }

public class ComponentsState(initialValue: Components = Components()) {

    public var components: Components by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<ComponentsState, *> = listSaver(
            save = { listOf(it.components) },
            restore = { ComponentsState(it[0]) },
        )
    }
}

@Composable
public fun rememberComponentsState(initialValue: Components = Components()): ComponentsState =
    rememberSaveable(saver = ComponentsState.Saver) { ComponentsState(initialValue) }
