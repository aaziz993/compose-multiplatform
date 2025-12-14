package clib.presentation.appbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.appbar.model.AppBar
import clib.presentation.noLocalProvidedFor

@Suppress("ComposeCompositionLocalUsage")
public val LocalAppBarState: ProvidableCompositionLocal<AppBarState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalAppBarState") }

public class AppBarState(initialValue: AppBar = AppBar()) {

    public var value: AppBar by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<AppBarState, *> = listSaver(
            save = { listOf(it.value) },
            restore = { AppBarState(it[0]) },
        )
    }
}

@Composable
public fun rememberAppBarState(initialValue: AppBar = AppBar()): AppBarState =
    rememberSaveable(saver = AppBarState.Saver) { AppBarState(initialValue) }
