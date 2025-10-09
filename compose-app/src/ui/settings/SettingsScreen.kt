package ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.model.Theme
import clib.presentation.theme.viewmodel.ThemeAction
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Settings

@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    route: Settings = Settings,
    theme: Theme = Theme(),
    themeAction: (ThemeAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
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
                title = { Text(text = "General") },
                contentPadding = PaddingValues(16.dp),
            ) {
                SettingsSwitch(
                    state = theme.isHighContrast,
                    title = { Text(text = "Theme") },
                    subtitle = { Text(text = "High contrast") },
                    modifier = Modifier,
                    enabled = true,
                    icon = { Icon(Icons.Outlined.Accessibility, "") },
                    onCheckedChange = { newState: Boolean -> themeAction(ThemeAction.SetTheme(theme.copy(isHighContrast = newState))) },
                )
            }
            SettingsGroup(
                modifier = Modifier,
                enabled = true,
                title = { Text(text = "Permissions") },
                contentPadding = PaddingValues(16.dp),
            ) {
//                SettingsSwitch(
//                    state = state.isCameraGranted,
//                    title = { Text(text = "Camera") },
//                    subtitle = { Text(text = "Get camera permission") },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.CameraAlt, "") },
//                    onCheckedChange = { newState: Boolean -> action(SettingsAction.GetPermission(Permission.CAMERA)) },
//                )
//
//                SettingsSwitch(
//                    state = state.isMicGranted,
//                    title = { Text(text = "Microphone") },
//                    subtitle = { Text(text = "Get microphone permission") },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.Mic, "") },
//                    onCheckedChange = { newState: Boolean -> action(SettingsAction.GetPermission(Permission.RECORD_AUDIO)) },
//                )
//
//                SettingsSwitch(
//                    state = state.isLocationGranted,
//                    title = { Text(text = "Geolocation") },
//                    subtitle = { Text(text = "Get geolocation permission") },
//                    modifier = Modifier,
//                    enabled = true,
//                    icon = { Icon(Icons.Outlined.LocationOn, "") },
//                    onCheckedChange = { newState: Boolean -> action(SettingsAction.GetPermission(Permission.LOCATION)) },
//                )
            }
        }
    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()
