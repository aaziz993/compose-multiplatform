package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import clib.presentation.theme.model.Theme

public class ThemeState(theme: Theme = Theme()) {

    public var theme: Theme by mutableStateOf(theme)

    public companion object {

        public val Saver: Saver<ThemeState, *> = listSaver(
            save = { listOf(it.theme) },
            restore = { ThemeState(it[0]) },
        )
    }
}

@Composable
public fun rememberThemeState(state: ThemeState = ThemeState()): ThemeState = rememberSaveable(saver = ThemeState.Saver) { state }

@Composable
public fun rememberTheme(theme: Theme = Theme()): ThemeState = rememberThemeState(ThemeState(theme))
