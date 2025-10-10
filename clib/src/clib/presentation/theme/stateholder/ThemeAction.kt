package clib.presentation.theme.stateholder

import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode

public sealed interface ThemeAction {
    public data class SetTheme(val value: Theme) : ThemeAction
    public data class SetMode(val value: ThemeMode) : ThemeAction
    public data class SetHighContrast(val value: Boolean) : ThemeAction
}
