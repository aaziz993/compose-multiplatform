package ui.settings.viewmodel

import clib.presentation.theme.model.Theme
import klib.data.location.locale.Locale
import klib.data.permission.model.Permission

public sealed interface SettingsAction {
    public data class SetLocale(val locale: Locale) : SettingsAction
    public data class SetTheme(val theme: Theme) : SettingsAction
    public data class GetPermission(val permission: Permission) : SettingsAction
}
