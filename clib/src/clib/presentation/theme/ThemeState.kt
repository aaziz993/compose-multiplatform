package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.theme.model.Theme

@Suppress("ComposeCompositionLocalUsage")
public val LocalThemeState: ProvidableCompositionLocal<ThemeState> =
    staticCompositionLocalOf(::ThemeState)

public class ThemeState(initialValue: Theme = Theme()) {

    public var theme: Theme by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<ThemeState, *> = listSaver(
            save = { listOf(it.theme) },
            restore = { ThemeState(it[0]) },
        )
    }
}

@Composable
public fun rememberThemeState(initialValue: Theme = Theme()): ThemeState =
    rememberSaveable(saver = ThemeState.Saver) { ThemeState(initialValue) }
