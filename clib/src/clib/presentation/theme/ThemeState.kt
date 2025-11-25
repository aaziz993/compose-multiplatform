package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import clib.presentation.theme.model.BaseTheme
import clib.presentation.theme.model.Theme

public class ThemeState(initialValue: BaseTheme = Theme()) {

    public var theme: BaseTheme by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<ThemeState, *> = listSaver(
            save = { listOf(it.theme) },
            restore = { ThemeState(it[0]) },
        )
    }
}

@Composable
public fun rememberThemeState(initialValue: BaseTheme = Theme()): ThemeState =
    rememberSaveable(saver = ThemeState.Saver) { ThemeState(initialValue) }
