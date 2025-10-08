package ui.settings.viewmodel

import clib.presentation.theme.ThemeState
import klib.data.permission.model.Permission

public data class SettingsState(
    val themeState: ThemeState = ThemeState(),
    val permissions: Set<Permission> = emptySet()
) {

    public val isCameraGranted: Boolean
        get() = Permission.CAMERA in permissions

    public val isMicGranted: Boolean
        get() = Permission.RECORD_AUDIO in permissions

    public val isLocationGranted: Boolean
        get() = Permission.LOCATION in permissions
}
