package ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clib.data.permission.BindEffect
import clib.data.permission.rememberPermissions
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.auth.LocalAppAuth
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.stateholder.ThemeAction
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
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.verify
import klib.data.permission.model.Permission
import klib.data.type.auth.model.Auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Settings
import ui.navigation.presentation.Verification

@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    route: Settings = Settings,
    onThemeAction: (ThemeAction) -> Unit = {},
    onAuthAction: (AuthAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val permissions = rememberPermissions(permissionController)

    BindEffect(permissionController)

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier =
                Modifier
                    .consumeWindowInsets(padding)
                    .verticalScroll(scrollState)
                    .padding(top = padding.calculateTopPadding()),
        ) {
            SettingsGroup(
                modifier = Modifier,
                enabled = true,
                title = { Text(text = stringResource(Res.string.general)) },
                contentPadding = PaddingValues(16.dp),
            ) {
                val theme = LocalAppTheme.current
                SettingsSwitch(
                    state = theme.isHighContrast,
                    title = { Text(text = stringResource(Res.string.high_contrast)) },
                    subtitle = { Text(text = stringResource(Res.string.enable_high_contrast)) },
                    modifier = Modifier,
                    enabled = true,
                    icon = { Icon(Icons.Outlined.Accessibility, "") },
                    onCheckedChange = { newState: Boolean -> onThemeAction(ThemeAction.SetTheme(theme.copy(isHighContrast = newState))) },
                )
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
                            permissionController.providePermission(Permission.CAMERA)
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
                            permissionController.providePermission(Permission.RECORD_AUDIO)
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
                            permissionController.providePermission(Permission.LOCATION)
                        }
                    },
                )
            }

            SettingsGroup(
                modifier = Modifier,
                enabled = true,
                title = { Text(text = "User") },
                contentPadding = PaddingValues(16.dp),
            ) {
                if (LocalAppAuth.current.user?.roles?.contains("Verified") == false)
                    Button(
                        onClick = {
                            onNavigationAction(NavigationAction.TypeNavigation.Navigate(Verification))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(Res.string.verify))
                    }

                Button(
                    onClick = {
                        onAuthAction(AuthAction.SetAuth(Auth()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(Res.string.sign_out))
                }
            }
        }
    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()
