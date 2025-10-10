package ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import clib.data.permission.BindEffect
import clib.data.permission.rememberPermissions
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.model.Theme
import clib.presentation.theme.stateholder.ThemeAction
import klib.data.type.auth.model.Auth
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Settings

@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    route: Settings = Settings,
    theme: Theme = Theme(),
    themeAction: (ThemeAction) -> Unit = {},
    auth: Auth = Auth(),
    authAction: (AuthAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }
    val permissions = rememberPermissions(permissionController)

    BindEffect(permissionController)
//
//    Scaffold(
//        modifier = modifier,
//        containerColor = MaterialTheme.colorScheme.surfaceContainer,
//    ) { padding ->
//        val scrollState = rememberScrollState()
//        Column(
//            modifier =
//                Modifier
//                    .consumeWindowInsets(padding)
//                    .verticalScroll(scrollState)
//                    .padding(top = padding.calculateTopPadding()),
//        ) {
//            SettingsGroup(
//                modifier = Modifier,
//                enabled = true,
//                title = { Text(text = "General") },
//                contentPadding = PaddingValues(16.dp),
//            ) {
//                SettingsSwitch(
//                    state = theme.isHighContrast,
//                    title = { Text(text = stringResource(Res.string.high_contrast)) },
//                    subtitle = { Text(text = stringResource(Res.string.enable_high_contrast)) },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.Accessibility, "") },
//                    onCheckedChange = { newState: Boolean -> themeAction(ThemeAction.SetTheme(theme.copy(isHighContrast = newState))) },
//                )
//            }
//            SettingsGroup(
//                modifier = Modifier,
//                enabled = true,
//                title = { Text(text = stringResource(Res.string.permissions)) },
//                contentPadding = PaddingValues(16.dp),
//            ) {
//                SettingsSwitch(
//                    state = Permission.CAMERA in permissions,
//                    title = { Text(text = stringResource(Res.string.camera)) },
//                    subtitle = { Text(text = stringResource(Res.string.get_camera_permission)) },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.CameraAlt, "") },
//                    onCheckedChange = { newState: Boolean ->
//                        coroutineScope.launch {
//                            permissionController.providePermission(Permission.CAMERA)
//                        }
//                    },
//                )
//
//                SettingsSwitch(
//                    state = Permission.RECORD_AUDIO in permissions,
//                    title = { Text(text = stringResource(Res.string.microphone)) },
//                    subtitle = { Text(text = stringResource(Res.string.get_microphone_permission)) },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.Mic, "") },
//                    onCheckedChange = { newState: Boolean ->
//                        coroutineScope.launch {
//                            permissionController.providePermission(Permission.RECORD_AUDIO)
//                        }
//                    },
//                )
//
//                SettingsSwitch(
//                    state = Permission.LOCATION in permissions,
//                    title = { Text(text = stringResource(Res.string.location)) },
//                    subtitle = { Text(text = stringResource(Res.string.get_location_permission)) },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.LocationOn, "") },
//                    onCheckedChange = { newState: Boolean ->
//                        coroutineScope.launch {
//                            permissionController.providePermission(Permission.LOCATION)
//                        }
//                    },
//                )
//            }
//
//            SettingsGroup(
//                modifier = Modifier,
//                enabled = true,
//                title = { Text(text = "User") },
//                contentPadding = PaddingValues(16.dp),
//            ) {
//                Button(
//                    onClick = {
//                        authAction(AuthAction.SetAuth(Auth()))
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                ) {
//                    Text(text = stringResource(Res.string.verify))
//                }
//
//                Button(
//                    onClick = {
//                        authAction(AuthAction.SetAuth(Auth()))
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                ) {
//                    Text(text = stringResource(Res.string.sign_out))
//                }
//            }
//        }
//    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()
