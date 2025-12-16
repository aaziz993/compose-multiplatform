package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.material.icons.automirrored.outlined.BluetoothSearching
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DensityLarge
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.BrowseGallery
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DynamicForm
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import clib.data.location.country.getEmojiFlag
import clib.data.type.primitives.string.getString
import clib.data.type.primitives.string.stringResource
import clib.presentation.appbar.model.AppBar
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.settings.SettingsLocalePickerDialog
import clib.presentation.config.RouteConfig
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.locale.LocalLocalization
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.density.toFloatPx
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.action_icon_content_color
import compose_app.generated.resources.app_bar
import compose_app.generated.resources.app_bar_avatar
import compose_app.generated.resources.app_bar_locales
import compose_app.generated.resources.app_bar_support
import compose_app.generated.resources.app_bar_themes
import compose_app.generated.resources.avatar_connectivity_indicator
import compose_app.generated.resources.background_location
import compose_app.generated.resources.bluetooth_advertise
import compose_app.generated.resources.bluetooth_connect
import compose_app.generated.resources.bluetooth_le
import compose_app.generated.resources.bluetooth_scan
import compose_app.generated.resources.camera
import compose_app.generated.resources.clear
import compose_app.generated.resources.coarse_location
import compose_app.generated.resources.color_scheme
import compose_app.generated.resources.connectivity
import compose_app.generated.resources.connectivity_alert
import compose_app.generated.resources.connectivity_indicator
import compose_app.generated.resources.connectivity_indicator_text
import compose_app.generated.resources.connectivity_snackbar
import compose_app.generated.resources.contact
import compose_app.generated.resources.container_color
import compose_app.generated.resources.dark_time
import compose_app.generated.resources.dark_time_lt_light_time
import compose_app.generated.resources.density
import compose_app.generated.resources.done
import compose_app.generated.resources.dynamic_color_scheme
import compose_app.generated.resources.expressive
import compose_app.generated.resources.font_scale
import compose_app.generated.resources.gallery
import compose_app.generated.resources.height
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.light_time
import compose_app.generated.resources.locale
import compose_app.generated.resources.location
import compose_app.generated.resources.navigation_icon_content_color
import compose_app.generated.resources.permission
import compose_app.generated.resources.record_audio
import compose_app.generated.resources.recovery
import compose_app.generated.resources.remote_notification
import compose_app.generated.resources.reset
import compose_app.generated.resources.route
import compose_app.generated.resources.scrolled_container_color
import compose_app.generated.resources.search
import compose_app.generated.resources.sensors
import compose_app.generated.resources.shapes
import compose_app.generated.resources.storage
import compose_app.generated.resources.subtitle_content_color
import compose_app.generated.resources.theme
import compose_app.generated.resources.title
import compose_app.generated.resources.title_content_color
import compose_app.generated.resources.typography
import compose_app.generated.resources.write_storage
import compose_app.generated.resources.open
import data.location.locale.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.permission.model.Permission
import kotlinx.coroutines.launch
import presentation.components.settings.SettingsColorPickerBottomSheet
import presentation.components.settings.SettingsMenuLink
import presentation.components.settings.SettingsSliderFinished
import presentation.components.settings.SettingsSwitch
import presentation.components.settings.SettingsTimePickerDialog
import presentation.connectivity.filledImageVector
import presentation.connectivity.outlinedImageVector
import presentation.theme.model.isDarkIcon
import presentation.theme.model.isDarkStringResource
import ui.navigation.presentation.SettingsColorScheme
import ui.navigation.presentation.SettingsDynamicColorScheme
import ui.navigation.presentation.SettingsMain
import ui.navigation.presentation.SettingsRoute
import ui.navigation.presentation.SettingsShapes
import ui.navigation.presentation.SettingsTypography

@Composable
public fun SettingsMainScreen(
    modifier: Modifier = Modifier,
    route: SettingsMain = SettingsMain,
    connectivityStatus: Status = Status.Disconnected,
    defaultAppBar: AppBar = AppBar(),
    appBar: AppBar = defaultAppBar,
    onAppBarChange: (AppBar) -> Unit = {},
    defaultConnectivity: Connectivity = Connectivity(),
    connectivity: Connectivity = defaultConnectivity,
    onConnectivityChange: (Connectivity) -> Unit = {},
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
    defaultRoutes: Map<String, RouteConfig> = emptyMap(),
    routes: Map<String, RouteConfig> = defaultRoutes,
    onRouteChange: (String, RouteConfig) -> Unit = { _, _ -> },
    permissions: Set<Permission> = emptySet(),
    onPermissionChange: (Permission?) -> Unit = { true },
    onOpenPermissions: () -> Unit = {},
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val coroutineScope = rememberCoroutineScope()

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.app_bar)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsSliderFinished(
            title = stringResource(Res.string.height),
            initialValue = appBar.expandedHeight.toFloatPx(),
            icon = { Icons.Default.Height },
            enabled = true,
            valueRange = 32.dp.toFloatPx()..96.dp.toFloatPx(),
        ) { value ->
            onAppBarChange(appBar.copy(expandedHeight = value.dp))
        }

        SettingsSwitch(
            title = stringResource(Res.string.title),
            value = appBar.isTitle,
            trueIcon = Icons.Filled.FlashOn,
            falseIcon = Icons.Outlined.FlashOn,
            onCheckedChange = { value ->
                onAppBarChange(appBar.copy(isTitle = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.app_bar_support),
            value = appBar.isSupport,
            trueIcon = Icons.Filled.FlashOn,
            falseIcon = Icons.Outlined.FlashOn,
            onCheckedChange = { value ->
                onAppBarChange(appBar.copy(isSupport = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.app_bar_themes),
            value = appBar.isTheme,
            trueIcon = Icons.Filled.FlashOn,
            falseIcon = Icons.Outlined.FlashOn,
            onCheckedChange = { value ->
                onAppBarChange(appBar.copy(isTheme = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.app_bar_locales),
            value = appBar.isLocale,
            trueIcon = Icons.Filled.FlashOn,
            falseIcon = Icons.Outlined.FlashOn,
            onCheckedChange = { value ->
                onAppBarChange(appBar.copy(isLocale = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.app_bar_avatar),
            value = appBar.isAvatar,
            trueIcon = Icons.Filled.FlashOn,
            falseIcon = Icons.Outlined.FlashOn,
            onCheckedChange = { value ->
                onAppBarChange(appBar.copy(isAvatar = value))
            },
        )

        val colors = appBar.colors
        val copyColors = appBar.copyColors(theme.isHighContrast)

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.container_color),
            colors.containerColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(containerColor = value)))
        }

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.scrolled_container_color),
            colors.scrolledContainerColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(scrolledContainerColor = value)))
        }

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.navigation_icon_content_color),
            colors.navigationIconContentColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(navigationIconContentColor = value)))
        }

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.title_content_color),
            colors.titleContentColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(titleContentColor = value)))
        }

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.action_icon_content_color),
            colors.actionIconContentColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(actionIconContentColor = value)))
        }

        SettingsColorPickerBottomSheet(
            stringResource(Res.string.subtitle_content_color),
            colors.subtitleContentColor,
        ) { value ->
            onAppBarChange(copyColors(colors.copy(subtitleContentColor = value)))
        }
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.connectivity)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        val connectivityTrueIcon = connectivityStatus.filledImageVector()
        val connectivityFalseIcon = connectivityStatus.outlinedImageVector()

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_alert),
            value = connectivity.isConnectivityAlert,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onConnectivityChange(connectivity.copy(isConnectivityAlert = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_snackbar),
            value = connectivity.isConnectivitySnackbar,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onConnectivityChange(connectivity.copy(isConnectivitySnackbar = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_indicator),
            value = connectivity.isConnectivityIndicator,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onConnectivityChange(connectivity.copy(isConnectivityIndicator = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.connectivity_indicator_text),
            value = connectivity.isConnectivityIndicatorText,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onConnectivityChange(connectivity.copy(isConnectivityIndicatorText = value))
            },
        )

        SettingsSwitch(
            title = stringResource(Res.string.avatar_connectivity_indicator),
            value = connectivity.isAvatarConnectivityIndicator,
            trueIcon = connectivityTrueIcon,
            falseIcon = connectivityFalseIcon,
            onCheckedChange = { value ->
                onConnectivityChange(connectivity.copy(isAvatarConnectivityIndicator = value))
            },
        )
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.theme)) },
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

        SettingsTimePickerDialog(
            title = stringResource(Res.string.light_time),
            value = theme.lightTime,
            enabled = true,
            subtitle = { Text(theme.lightTime.toString()) },
        ) { value ->
            onThemeChange(theme.copy(lightTime = value))
            false
        }

        val localization = LocalLocalization.current

        SettingsTimePickerDialog(
            title = stringResource(Res.string.dark_time),
            value = theme.darkTime,
            enabled = true,
            subtitle = { Text(theme.darkTime.toString()) },
        ) { value ->
            // Dark time should be greater than light time.
            if (value > theme.lightTime) {
                onThemeChange(theme.copy(darkTime = value))
                false
            }
            else {
                coroutineScope.launch {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent(getString(Res.string.dark_time_lt_light_time, localization)),
                    )
                }
                true
            }
        }

        SettingsSwitch(
            title = stringResource(Res.string.dynamic_color_scheme),
            value = theme.isDynamic,
            trueIcon = Icons.Filled.DynamicForm,
            falseIcon = Icons.Outlined.DynamicForm,
        ) { value ->
            onThemeChange(theme.copy(isDynamic = value))
        }

        SettingsSwitch(
            title = stringResource(Res.string.high_contrast),
            value = theme.isHighContrast,
            trueIcon = Icons.Filled.Contrast,
            falseIcon = Icons.Outlined.Contrast,
        ) { value ->
            onThemeChange(theme.copy(isHighContrast = value))
        }

        SettingsMenuLink(
            title = stringResource(Res.string.color_scheme),
            enabled = true,
            icon = Icons.Default.Palette,
        ) {
            onNavigationActions(
                arrayOf(
                    NavigationAction.Push(SettingsColorScheme),
                ),
            )
        }

        SettingsMenuLink(
            title = stringResource(Res.string.dynamic_color_scheme),
            enabled = true,
            icon = Icons.Default.DynamicForm,
        ) {
            onNavigationActions(
                arrayOf(
                    NavigationAction.Push(SettingsDynamicColorScheme),
                ),
            )
        }

        SettingsSwitch(
            stringResource(Res.string.expressive),
            value = theme.isExpressive,
            trueIcon = Icons.Filled.AutoAwesomeMotion,
            falseIcon = Icons.Outlined.AutoAwesomeMotion,
        ) { state ->
            onThemeChange(theme.copy(isExpressive = !theme.isExpressive))
        }

        SettingsMenuLink(
            title = stringResource(Res.string.shapes),
            enabled = true,
            icon = Icons.Default.FormatShapes,
        ) {
            onNavigationActions(
                arrayOf(
                    NavigationAction.Push(SettingsShapes),
                ),
            )
        }

        SettingsMenuLink(
            title = stringResource(Res.string.typography),
            enabled = true,
            icon = Icons.Default.TextFormat,
        ) {
            onNavigationActions(
                arrayOf(
                    NavigationAction.Push(SettingsTypography),
                ),
            )
        }
    }
    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.density)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsSliderFinished(
            title = stringResource(Res.string.density),
            initialValue = density.density,
            icon = { value ->
                when {
                    value < 2 -> Icons.Default.DensitySmall
                    value < 2.5 -> Icons.Default.DensityMedium
                    else -> Icons.Default.DensityLarge
                }
            },
            enabled = true,
            valueRange = 1.5f..2.5f,
            steps = 1,
        ) { value ->
            onDensityChange(Density(value, density.fontScale))
        }

        SettingsSliderFinished(
            title = stringResource(Res.string.font_scale),
            initialValue = density.fontScale,
            icon = { Icons.Default.LinearScale },
            enabled = true,
            valueRange = 1f..2f,
            steps = 1,
        ) { value ->
            onDensityChange(Density(density.density, value))
        }
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.locale)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsLocalePickerDialog(
            title = { Text(text = stringResource(Res.string.locale)) },
            icon = { Text(locale.country()!!.alpha2.getEmojiFlag()) },
            subtitle = { Text(locale.asStringResource()) },
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            enabled = true,
            dialogModifier = Modifier.clip(RoundedCornerShape(12.dp)),
            locales = locales,
            country = { locale ->
                locale.country()!!.copy(name = locale.asStringResource())
            },
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.locale),
                searchHint = stringResource(Res.string.search),
                clear = stringResource(Res.string.clear),
                showCountryDial = false,
            ),
        ) { value ->
            onLocaleChange(value)
            false
        }
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.route)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsMenuLink(
            title = stringResource(Res.string.route),
            enabled = true,
            icon = Icons.Default.Route,
        ) {
            onNavigationActions(
                arrayOf(
                    NavigationAction.Push(SettingsRoute),
                ),
            )
        }
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
            trueIcon = Icons.Filled.CameraAlt,
            falseIcon = Icons.Outlined.CameraAlt,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.gallery),
            value = Permission.GALLERY,
            trueIcon = Icons.Filled.BrowseGallery,
            falseIcon = Icons.Outlined.BrowseGallery,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.storage),
            value = Permission.STORAGE,
            trueIcon = Icons.Filled.Storage,
            falseIcon = Icons.Outlined.Storage,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.write_storage),
            value = Permission.WRITE_STORAGE,
            trueIcon = Icons.Filled.Storage,
            falseIcon = Icons.Outlined.Storage,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.location),
            value = Permission.LOCATION,
            trueIcon = Icons.Filled.MyLocation,
            falseIcon = Icons.Outlined.MyLocation,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.coarse_location),
            value = Permission.COARSE_LOCATION,
            trueIcon = Icons.Filled.MyLocation,
            falseIcon = Icons.Outlined.MyLocation,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.background_location),
            value = Permission.BACKGROUND_LOCATION,
            trueIcon = Icons.Filled.LocationOn,
            falseIcon = Icons.Outlined.LocationOff,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.bluetooth_le),
            value = Permission.BLUETOOTH_LE,
            trueIcon = Icons.Filled.Bluetooth,
            falseIcon = Icons.Outlined.Bluetooth,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.remote_notification),
            value = Permission.REMOTE_NOTIFICATION,
            trueIcon = Icons.Filled.NotificationsActive,
            falseIcon = Icons.Outlined.NotificationsOff,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.record_audio),
            value = Permission.RECORD_AUDIO,
            trueIcon = Icons.Filled.RecordVoiceOver,
            falseIcon = Icons.Outlined.RecordVoiceOver,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.bluetooth_scan),
            value = Permission.BLUETOOTH_SCAN,
            trueIcon = Icons.AutoMirrored.Filled.BluetoothSearching,
            falseIcon = Icons.AutoMirrored.Outlined.BluetoothSearching,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.bluetooth_advertise),
            value = Permission.BLUETOOTH_ADVERTISE,
            trueIcon = Icons.Filled.Bluetooth,
            falseIcon = Icons.Outlined.Bluetooth,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.bluetooth_connect),
            value = Permission.BLUETOOTH_CONNECT,
            trueIcon = Icons.Filled.BluetoothConnected,
            falseIcon = Icons.Outlined.BluetoothConnected,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.contact),
            value = Permission.CONTACT,
            trueIcon = Icons.Filled.Contacts,
            falseIcon = Icons.Outlined.Contacts,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.sensors),
            value = Permission.SENSORS,
            trueIcon = Icons.Filled.Sensors,
            falseIcon = Icons.Outlined.Sensors,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsMenuLink(
            title = stringResource(Res.string.open),
            enabled = true,
            icon = Icons.Default.AppSettingsAlt,
            onClick = onOpenPermissions,
        )
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.recovery)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        val resettable by remember(appBar, connectivity, theme, density, locale, routes) {
            derivedStateOf {
                appBar != defaultAppBar ||
                    connectivity != defaultConnectivity ||
                    theme != defaultTheme ||
                    density != defaultDensity ||
                    locale != defaultLocale ||
                    routes.any { (key, value) -> defaultRoutes[key] != value }
            }
        }

        SettingsMenuLink(
            title = stringResource(Res.string.reset),
            enabled = true,
            icon = if (resettable) Icons.Outlined.Restore else Icons.Filled.Restore,
            subtitle = stringResource(if (resettable) Res.string.reset else Res.string.done),
        ) {
            if (appBar != defaultAppBar) onAppBarChange(defaultAppBar)
            if (connectivity != defaultConnectivity) onConnectivityChange(defaultConnectivity)
            if (theme != defaultTheme) onThemeChange(defaultTheme)
            if (density != defaultDensity) onDensityChange(defaultDensity)
            if (locale != defaultLocale) onLocaleChange(defaultLocale)
            if (routes != defaultRoutes) defaultRoutes.forEach { (route, value) -> onRouteChange(route, value) }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsMainScreen(): Unit = SettingsMainScreen()
