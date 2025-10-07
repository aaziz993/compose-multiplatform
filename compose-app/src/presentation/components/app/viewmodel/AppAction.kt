package presentation.components.app.viewmodel

import clib.presentation.theme.model.Theme
import klib.data.location.locale.Locale

public sealed class AppAction {
    public data class SetLocale(val locale: Locale) : AppAction()
    public data class SetTheme(val theme: Theme) : AppAction()
}
