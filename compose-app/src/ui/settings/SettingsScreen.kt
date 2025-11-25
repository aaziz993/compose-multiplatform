package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.permission.BindEffect
import clib.data.permission.rememberPermissions
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import compose_app.generated.resources.Res
import compose_app.generated.resources.camera
import compose_app.generated.resources.enable_high_contrast
import compose_app.generated.resources.general
import compose_app.generated.resources.get_camera_permission
import compose_app.generated.resources.get_location_permission
import compose_app.generated.resources.get_microphone_permission
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.location
import compose_app.generated.resources.microphone
import compose_app.generated.resources.permissions
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.model.Permission
import klib.data.type.auth.model.Auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.navigation.presentation.Settings

@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    route: Settings = Settings,
    theme: Theme = Theme(),
    onThemeChange: (Theme) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val coroutineScope = rememberCoroutineScope()

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val permissions = rememberPermissions(permissionsController)

    BindEffect(permissionsController)

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.general)) },
        contentPadding = PaddingValues(16.dp),
    ) {
//        SettingsSwitch(
//            state = theme.isHighContrast,
//            title = { Text(text = stringResource(Res.string.high_contrast)) },
//            subtitle = { Text(text = stringResource(Res.string.enable_high_contrast)) },
//            modifier = Modifier,
//            enabled = true,
//            icon = { Icon(Icons.Outlined.Accessibility, "") },
//            onCheckedChange = { newState: Boolean -> onThemeChange(theme.copy(isHighContrast = newState)) },
//        )
    }
    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.permissions)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsSwitch(
            state = Permission.CAMERA in permissions,
            title = { Text(text = stringResource(Res.string.camera)) },
            subtitle = { Text(text = stringResource(Res.string.get_camera_permission)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Outlined.CameraAlt, "") },
            onCheckedChange = { newState: Boolean ->
                coroutineScope.launch {
                    permissionsController.providePermission(Permission.CAMERA)

                    try {
                        permissionsController.providePermission(Permission.GALLERY)
                    }
                    catch (deniedAlways: PermissionDeniedAlwaysException) {
                        GlobalSnackbarEventController.sendEvent(SnackbarEvent(deniedAlways.message.orEmpty()))
                    }
                    catch (denied: PermissionDeniedException) {
                        GlobalSnackbarEventController.sendEvent(SnackbarEvent(denied.message.orEmpty()))
                    }
                }
            },
        )

        SettingsSwitch(
            state = Permission.RECORD_AUDIO in permissions,
            title = { Text(text = stringResource(Res.string.microphone)) },
            subtitle = { Text(text = stringResource(Res.string.get_microphone_permission)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Outlined.Mic, "") },
            onCheckedChange = { newState: Boolean ->
                coroutineScope.launch {
                    permissionsController.providePermission(Permission.RECORD_AUDIO)
                }
            },
        )

        SettingsSwitch(
            state = Permission.LOCATION in permissions,
            title = { Text(text = stringResource(Res.string.location)) },
            subtitle = { Text(text = stringResource(Res.string.get_location_permission)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Outlined.LocationOn, "") },
            onCheckedChange = { newState: Boolean ->
                coroutineScope.launch {
                    permissionsController.providePermission(Permission.LOCATION)
                }
            },
        )
    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()
