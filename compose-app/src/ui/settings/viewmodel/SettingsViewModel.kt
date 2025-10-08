package ui.settings.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.theme.ThemeState
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.cache.Cache
import klib.data.cache.SettingsCache
import klib.data.permission.PermissionsController
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.isPermissionGranted
import klib.data.permission.model.Permission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class SettingsViewModel(
    private val permissionsController: PermissionsController,
) : AbstractViewModel<SettingsAction>() {

    public val state: RestartableStateFlow<SettingsState>
        field = viewModelMutableStateFlow(SettingsState()) { state ->
            state.copy(permissions = getGrantedPermissions())
        }

    override fun action(action: SettingsAction): Unit = when (action) {
        is SettingsAction.SetTheme -> state.value.themeState.theme = action.theme
        is SettingsAction.SetLocale -> {}
        is SettingsAction.GetPermission -> getPermission(action.permission)
    }

    private fun getPermission(permission: Permission) {
        viewModelScope.launch {
            try {
                permissionsController.getPermissions(permission)
                state.update { state -> state.copy(permissions = getGrantedPermissions()) }
            }
            catch (deniedAlways: PermissionDeniedAlwaysException) {
                GlobalSnackbarEventController.sendEvent(SnackbarEvent("Permission denied always"))
            }
            catch (denied: PermissionDeniedException) {
                GlobalSnackbarEventController.sendEvent(SnackbarEvent("Permission denied"))
            }
        }
    }

    private suspend fun getGrantedPermissions() = Permission.entries.filter { permission ->
        permissionsController.isPermissionGranted(permission)
    }.toSet()
}

