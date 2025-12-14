package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DensityLarge
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DynamicForm
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Highlight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import clib.data.location.country.getEmojiFlag
import clib.data.type.primitives.string.stringResource
import clib.presentation.appbar.model.AppBar
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.settings.SettingsLocalePickerDialog
import clib.presentation.config.RouteConfig
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_bar_avatar
import compose_app.generated.resources.app_bar_locales
import compose_app.generated.resources.app_bar_support
import compose_app.generated.resources.app_bar_themes
import compose_app.generated.resources.appearance
import compose_app.generated.resources.avatar_connectivity_indicator
import compose_app.generated.resources.camera
import compose_app.generated.resources.clear
import compose_app.generated.resources.color_scheme
import compose_app.generated.resources.connectivity_alert
import compose_app.generated.resources.connectivity_indicator
import compose_app.generated.resources.connectivity_indicator_text
import compose_app.generated.resources.connectivity_snackbar
import compose_app.generated.resources.density
import compose_app.generated.resources.done
import compose_app.generated.resources.dynamic_color_scheme
import compose_app.generated.resources.expressive
import compose_app.generated.resources.font_scale
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.locale
import compose_app.generated.resources.location
import compose_app.generated.resources.microphone
import compose_app.generated.resources.permission
import compose_app.generated.resources.recovery
import compose_app.generated.resources.reset
import compose_app.generated.resources.route
import compose_app.generated.resources.search
import compose_app.generated.resources.shapes
import compose_app.generated.resources.theme
import compose_app.generated.resources.title
import compose_app.generated.resources.typography
import data.location.locale.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import klib.data.auth.model.Auth
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.permission.model.Permission
import presentation.components.settings.SettingsMenuLink
import presentation.components.settings.SettingsSliderFinished
import presentation.components.settings.SettingsSwitch
import presentation.connectivity.filledImageVector
import presentation.connectivity.outlinedImageVector
import presentation.theme.model.isDarkIcon
import presentation.theme.model.isDarkStringResource
import ui.navigation.presentation.SettingsColorScheme
import ui.navigation.presentation.SettingsDynamicColorScheme
import ui.navigation.presentation.SettingsMain
import ui.navigation.presentation.SettingsRoutes
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
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    permissions: Set<Permission> = emptySet(),
    onPermissionChange: (Permission?) -> Unit = { true },
    defaultRoutes: Map<String, RouteConfig> = emptyMap(),
    routes: Map<String, RouteConfig> = defaultRoutes,
    onRoutesChange: (String, RouteConfig) -> Unit = { _, _ -> },
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
            trueIcon = Icons.Filled.Highlight,
            falseIcon = Icons.Outlined.Highlight,
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
                    NavigationAction.PopTo(SettingsColorScheme),
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
                    NavigationAction.PopTo(SettingsDynamicColorScheme),
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
                    NavigationAction.PopTo(SettingsShapes),
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
                    NavigationAction.PopTo(SettingsTypography),
                    NavigationAction.Push(SettingsTypography),
                ),
            )
        }

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
            title = stringResource(Res.string.microphone),
            value = Permission.RECORD_AUDIO,
            trueIcon = Icons.Filled.Mic,
            falseIcon = Icons.Outlined.Mic,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )

        SettingsSwitch(
            title = stringResource(Res.string.location),
            value = Permission.LOCATION,
            trueIcon = Icons.Filled.LocationOn,
            falseIcon = Icons.Outlined.LocationOn,
            permissions = permissions,
            coroutineScope = coroutineScope,
            onCheckedChange = onPermissionChange,
        )
    }

    SettingsMenuLink(
        title = stringResource(Res.string.route),
        enabled = true,
        icon = Icons.Default.Route,
    ) {
        onNavigationActions(
            arrayOf(
                NavigationAction.PopTo(SettingsRoutes),
                NavigationAction.Push(SettingsRoutes),
            ),
        )
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.recovery)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        val resettable =
            appBar != defaultAppBar ||
                connectivity != defaultConnectivity ||
                theme != defaultTheme ||
                density != defaultDensity ||
                locale != defaultLocale
        routes != defaultRoutes

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
            if (routes != defaultRoutes) defaultRoutes.forEach { (route, config) -> onRoutesChange(route, config) }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsMainScreen(): Unit = SettingsMainScreen()
