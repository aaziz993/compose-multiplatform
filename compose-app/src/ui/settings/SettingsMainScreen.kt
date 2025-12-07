package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SignalCellular0Bar
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet4Bar
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.SignalCellular0Bar
import androidx.compose.material.icons.outlined.SignalCellularConnectedNoInternet4Bar
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.data.location.country.getEmojiFlag
import clib.presentation.components.Components
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.picker.ListPickerDialog
import clib.presentation.components.settings.SettingsLocalePickerDialog
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import compose_app.generated.resources.Res
import compose_app.generated.resources.appearance
import compose_app.generated.resources.avatar_connectivity_indicator
import compose_app.generated.resources.camera
import compose_app.generated.resources.color_palette
import compose_app.generated.resources.connectivity_alert
import compose_app.generated.resources.connectivity_indicator
import compose_app.generated.resources.connectivity_snackbar
import compose_app.generated.resources.density
import compose_app.generated.resources.dynamic_color_palette
import compose_app.generated.resources.expressive
import compose_app.generated.resources.font_scale
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.locale
import compose_app.generated.resources.location
import compose_app.generated.resources.microphone
import compose_app.generated.resources.permission
import compose_app.generated.resources.quick_access_to_avatar
import compose_app.generated.resources.quick_access_to_locales
import compose_app.generated.resources.quick_access_to_support
import compose_app.generated.resources.quick_access_to_themes
import compose_app.generated.resources.recovery
import compose_app.generated.resources.reset
import compose_app.generated.resources.search
import compose_app.generated.resources.clear
import compose_app.generated.resources.theme
import data.location.locale.asStringResource
import data.type.primitives.string.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import klib.data.auth.model.Auth
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.model.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsMenuLink
import presentation.components.settings.SettingsSliderPostpone
import presentation.components.settings.SettingsSwitch
import presentation.theme.model.isDarkIcon
import presentation.theme.model.isDarkStringResource
import ui.navigation.presentation.SettingsColorScheme
import ui.navigation.presentation.SettingsDynamicColorScheme
import ui.navigation.presentation.SettingsMain

@Composable
public fun SettingsMainScreen(
    modifier: Modifier = Modifier,
    route: SettingsMain = SettingsMain,
    connectivity: Status = Status.Disconnected,
    permissions: Set<Permission> = emptySet(),
    onPermissionChange: (Permission?) -> Unit = { true },
    defaultComponents: Components = Components(),
    components: Components = defaultComponents,
    onComponentsChange: (Components) -> Unit = {},
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
    defaultDensity: Density = Density(2f),
    density: Density = defaultDensity,
    onDensityChange: (Density) -> Unit = {},
    locales: List<Locale> = emptyList(),
    defaultLocale: Locale = Locale.current,
    locale: Locale = defaultLocale,
    onLocaleChange: (Locale) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val coroutineScope = rememberCoroutineScope()
    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.appearance)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsMenuLink(
            title = stringResource(Res.string.theme),
            enabled = true,
            icon = theme.isDarkIcon(),
            subtitle = theme.isDarkStringResource(),
        ) {
            onThemeChange(theme.copyIsDarkToggled())
        }

        SettingsSwitch(
            title = stringResource(Res.string.dynamic_color_palette),
            value = theme.isDynamic,
            trueIcon = Icons.Outlined.Palette,
            falseIcon = Icons.Filled.Palette,
        ) { value ->
            onThemeChange(theme.copy(isDynamic = value))
        }

        SettingsSwitch(
            title = stringResource(Res.string.high_contrast),
            value = theme.isHighContrast,
            trueIcon = Icons.Outlined.Accessibility,
            falseIcon = Icons.Filled.Accessibility,
        ) { value ->
            onThemeChange(theme.copy(isHighContrast = value))
        }

        SettingsMenuLink(
            title = stringResource(Res.string.color_palette),
            enabled = true,
            icon = Icons.Default.ColorLens,
        ) {
            onNavigationAction(NavigationAction.Push(SettingsColorScheme))
        }

        SettingsMenuLink(
            title = stringResource(Res.string.dynamic_color_palette),
            enabled = true,
            icon = Icons.Default.ColorLens,
        ) {
            onNavigationAction(NavigationAction.Push(SettingsDynamicColorScheme))
        }

        SettingsSwitch(
            stringResource(Res.string.expressive),
            value = theme.isExpressive,
            trueIcon = Icons.Outlined.Palette,
            falseIcon = Icons.Filled.Palette,
        ) { state ->
            onThemeChange(theme.copy(isExpressive = !theme.isExpressive))
        }

        SettingsSliderPostpone(
            title = stringResource(Res.string.density),
            initialValue = density.density,
            icon = Icons.Default.TouchApp,
            enabled = true,
            valueRange = 1.5f..2.5f,
            steps = 1,
        ) { value ->
            onDensityChange(Density(value, density.fontScale))
        }

        SettingsSliderPostpone(
            title = stringResource(Res.string.font_scale),
            initialValue = density.fontScale,
            icon = Icons.Default.TextFields,
            enabled = true,
            valueRange = 1f..2f,
            steps = 1,
        ) { value ->
            onDensityChange(Density(density.density, value))
        }

        SettingsLocalePickerDialog(
            title = { Text(text = stringResource(Res.string.locale)) },
            icon = { Text(locale.country()!!.alpha2.getEmojiFlag()) },
            subtitle = { Text(locale.asStringResource()) },
            modifier = Modifier,
            enabled = true,
            locales = locales,
            country = { locale ->
                locale.country()!!.copy(name = locale.asStringResource())
            },
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.locale),
                searchHint = stringResource(Res.string.search),
                clear = stringResource(Res.string.clear),
            ),
        ) { value ->
            onLocaleChange(value)
            false
        }

        val connectivityTrueIcon =
            if (connectivity.isConnected) Icons.Filled.SignalCellular0Bar
            else Icons.Filled.SignalCellularConnectedNoInternet4Bar

        val connectivityFalseIcon =
            if (connectivity.isConnected) Icons.Outlined.SignalCellular0Bar
            else Icons.Outlined.SignalCellularConnectedNoInternet4Bar

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_alert),
            value = components.connectivity.isConnectivityAlert,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        connectivity = components.connectivity.copy(isConnectivityAlert = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_snackbar),
            value = components.connectivity.isConnectivitySnackbar,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        connectivity = components.connectivity.copy(isConnectivitySnackbar = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_indicator),
            value = components.connectivity.isConnectivityIndicator,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        connectivity = components.connectivity.copy(isConnectivityIndicator = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.avatar_connectivity_indicator),
            value = components.connectivity.isAvatarConnectivityIndicator,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        connectivity = components.connectivity.copy(isAvatarConnectivityIndicator = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_support),
            value = components.quickAccess.isSupport,
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        quickAccess = components.quickAccess.copy(isSupport = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_themes),
            value = components.quickAccess.isTheme,
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        quickAccess = components.quickAccess.copy(isTheme = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_locales),
            value = components.quickAccess.isLocale,
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        quickAccess = components.quickAccess.copy(isLocale = value),
                    ),
                )
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_avatar),
            value = components.quickAccess.isAvatar,
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value ->
                onComponentsChange(
                    components.copy(
                        quickAccess = components.quickAccess.copy(isAvatar = value),
                    ),
                )
            },
        )
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.permission)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsSwitch(
            title = stringResource(Res.string.camera),
            value = Permission.CAMERA,
            trueIcon = Icons.Outlined.CameraAlt,
            falseIcon = Icons.Filled.CameraAlt,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.microphone),
            value = Permission.RECORD_AUDIO,
            trueIcon = Icons.Outlined.Mic,
            falseIcon = Icons.Filled.Mic,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.location),
            value = Permission.LOCATION,
            trueIcon = Icons.Outlined.LocationOn,
            falseIcon = Icons.Filled.LocationOn,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )
    }
    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.recovery)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        val canReset = theme != defaultTheme ||
            density != defaultDensity ||
            locale != defaultLocale ||
            components != defaultComponents

        SettingsMenuLink(
            title = stringResource(Res.string.reset),
            enabled = true,
            icon = if (canReset) Icons.Outlined.Restore else Icons.Filled.Restore,
            subtitle = if (canReset) stringResource(Res.string.reset) else "",
        ) {
            if (theme != defaultTheme) onThemeChange(defaultTheme)
            if (density != defaultDensity) onDensityChange(defaultDensity)
            if (locale != defaultLocale) onLocaleChange(defaultLocale)
            if (components != defaultComponents) onComponentsChange(defaultComponents)
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsMainScreen(): Unit = SettingsMainScreen()

@Suppress("ComposeParameterOrder")
@Composable
private fun SettingsSwitch(
    title: String,
    value: Permission,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    trueIcon: ImageVector,
    falseIcon: ImageVector,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    switchColors: SwitchColors =
        SwitchDefaults.colors(
            checkedTrackColor = colors.actionColor(enabled),
            checkedThumbColor = contentColorFor(colors.actionColor(enabled)),
            disabledCheckedTrackColor = colors.actionColor(enabled),
            disabledCheckedThumbColor = contentColorFor(colors.actionColor(enabled)),
        ),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    permissions: Set<Permission>,
    coroutineScope: CoroutineScope,
    onCheckedChange: (Permission?) -> Unit,
) = SettingsSwitch(
    title,
    value in permissions,
    modifier,
    enabled,
    trueIcon,
    falseIcon,
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange = {
        if (it) onCheckedChange(null)
        else {
            coroutineScope.launch {
                try {
                    onCheckedChange(value)
                }
                catch (deniedAlways: PermissionDeniedAlwaysException) {
                    GlobalSnackbarEventController.sendEvent(SnackbarEvent(deniedAlways.message.orEmpty()))
                }
                catch (denied: PermissionDeniedException) {
                    GlobalSnackbarEventController.sendEvent(SnackbarEvent(denied.message.orEmpty()))
                }
            }
        }
    },
)
