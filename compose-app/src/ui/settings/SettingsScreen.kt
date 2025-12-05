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
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.data.permission.BindEffect
import clib.data.permission.rememberPermissions
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.components.color.model.ColorPicker
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.settings.SettingsColorPickerBottomSheet
import clib.presentation.components.settings.SettingsLocalePickerDialog
import clib.presentation.components.settings.SettingsSlider
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.navigation.NavigationAction
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import compose_app.generated.resources.Res
import compose_app.generated.resources.alpha
import compose_app.generated.resources.appearance
import compose_app.generated.resources.blend
import compose_app.generated.resources.blue
import compose_app.generated.resources.brightness
import compose_app.generated.resources.camera
import compose_app.generated.resources.cancel
import compose_app.generated.resources.color
import compose_app.generated.resources.color_palette
import compose_app.generated.resources.copy
import compose_app.generated.resources.density
import compose_app.generated.resources.dynamic_color_palette
import compose_app.generated.resources.expressive
import compose_app.generated.resources.font_scale
import compose_app.generated.resources.green
import compose_app.generated.resources.grid
import compose_app.generated.resources.hex
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.hsla
import compose_app.generated.resources.hsv
import compose_app.generated.resources.left
import compose_app.generated.resources.lightness
import compose_app.generated.resources.locale
import compose_app.generated.resources.location
import compose_app.generated.resources.microphone
import compose_app.generated.resources.permission
import compose_app.generated.resources.quick_access_to_avatar
import compose_app.generated.resources.quick_access_to_locales
import compose_app.generated.resources.connectivity_indicator
import compose_app.generated.resources.avatar_connectivity_indicator
import compose_app.generated.resources.quick_access_to_support
import compose_app.generated.resources.quick_access_to_themes
import compose_app.generated.resources.recovery
import compose_app.generated.resources.red
import compose_app.generated.resources.reset
import compose_app.generated.resources.rgba
import compose_app.generated.resources.right
import compose_app.generated.resources.saturation
import compose_app.generated.resources.search
import compose_app.generated.resources.select
import compose_app.generated.resources.theme
import data.type.primitives.EnabledText
import data.type.primitives.SettingsSwitch
import data.type.primitives.string.asStringResource
import klib.data.auth.model.Auth
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.permission.PermissionsController
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.model.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import presentation.theme.model.IsDarkIcon
import presentation.theme.model.isDarkStringResource
import ui.navigation.presentation.Settings

@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    route: Settings = Settings,
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
    defaultQuickAccess: QuickAccess = QuickAccess(),
    quickAccess: QuickAccess = defaultQuickAccess,
    onQuickAccessChange: (QuickAccess) -> Unit = {},
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
        title = { Text(text = stringResource(Res.string.appearance)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.theme)) },
            subtitle = { Text(text = theme.isDarkStringResource()) },
            modifier = Modifier,
            enabled = true,
            icon = { theme.IsDarkIcon() },
            onClick = {
                onThemeChange(theme.copyIsDarkToggled())
            },
        )

        theme.isDynamic.SettingsSwitch(
            title = stringResource(Res.string.dynamic_color_palette),
            trueIcon = Icons.Outlined.Palette,
            falseIcon = Icons.Filled.Palette,
            onCheckedChange = { value -> onThemeChange(theme.copy(isDark = value)) },
        )

        theme.isHighContrast.SettingsSwitch(
            title = stringResource(Res.string.high_contrast),
            trueIcon = Icons.Outlined.Accessibility,
            falseIcon = Icons.Filled.Accessibility,
            onCheckedChange = { value -> onThemeChange(theme.copy(isHighContrast = value)) },
        )

        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.color_palette)) },
            subtitle = { Text(text = stringResource(Res.string.color_palette)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.ColorLens, stringResource(Res.string.color_palette)) },
            onClick = { },
        )

        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            subtitle = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.ColorLens, stringResource(Res.string.dynamic_color_palette)) },
            onClick = { },
        )

        SettingsColorPickerBottomSheet(
            value = Color.Cyan,
            onValueChanged = {},
            title = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            subtitle = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            modifier = Modifier,
            enabled = true,
            picker = ColorPicker(
                stringResource(Res.string.color),
                stringResource(Res.string.rgba),
                stringResource(Res.string.red),
                stringResource(Res.string.green),
                stringResource(Res.string.blue),
                stringResource(Res.string.alpha),
                stringResource(Res.string.grid),
                stringResource(Res.string.hsla),
                stringResource(Res.string.saturation),
                stringResource(Res.string.lightness),
                stringResource(Res.string.hsv),
                stringResource(Res.string.brightness),
                stringResource(Res.string.blend),
                stringResource(Res.string.left),
                stringResource(Res.string.right),
                stringResource(Res.string.hex),
                stringResource(Res.string.copy),
                stringResource(Res.string.cancel),
                stringResource(Res.string.select),
            ),
        )

        SettingsSwitch(
            state = theme.isExpressive,
            title = { Text(text = stringResource(Res.string.expressive)) },
            subtitle = {
                theme.isExpressive.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Palette, stringResource(Res.string.expressive)) },
            onCheckedChange = { state ->
                onThemeChange(theme.copy(isExpressive = !theme.isExpressive))
            },
        )

        var densityValue by remember { mutableFloatStateOf(density.density) }
        SettingsSlider(
            value = densityValue,
            valueRange = 1.5f..2.5f,
            steps = 1,
            title = { Text(stringResource(Res.string.density)) },
            subtitle = { Text(stringResource(Res.string.density)) },
            enabled = true,
            icon = { Icon(Icons.Default.TouchApp, stringResource(Res.string.density)) },
            onValueChange = { value, _ ->
                densityValue = value
            },
            onValueChangeFinished = {
                onDensityChange(Density(densityValue, density.fontScale))
            },
        )

        var fontScaleValue by remember { mutableFloatStateOf(density.fontScale) }
        SettingsSlider(
            value = fontScaleValue,
            valueRange = 1f..2f,
            steps = 1,
            title = { Text(stringResource(Res.string.font_scale)) },
            subtitle = { Text(stringResource(Res.string.font_scale)) },
            enabled = true,
            icon = { Icon(Icons.Default.TextFields, stringResource(Res.string.font_scale)) },
            onValueChange = { value, _ ->
                fontScaleValue = value
            },
            onValueChangeFinished = {
                onDensityChange(Density(density.density, fontScaleValue))
            },
        )

        SettingsLocalePickerDialog(
            value = locale,
            onValueChanged = { value ->
                onLocaleChange(value)
                true
            },
            title = { Text(text = stringResource(Res.string.locale)) },
            subtitle = {
                Text(
                    text = "locale_${
                        locale
                            .toString()
                            .replace('-', '_')
                            .lowercase()
                    }".asStringResource(),
                )
            },
            modifier = Modifier,
            enabled = true,
            locales = locales,
            country = { locale ->
                locale.country()!!.copy(
                    name = "locale_${
                        locale
                            .toString()
                            .replace('-', '_')
                            .lowercase()
                    }".asStringResource(),
                )
            },
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.locale),
                searchHint = stringResource(Res.string.search),
            ),
        )

        quickAccess.isConnectivityIndicator.SettingsSwitch(
            title = stringResource(Res.string.connectivity_indicator),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isConnectivityIndicator = value)) },
        )

        quickAccess.isSupport.SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_support),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isSupport = value)) },
        )

        quickAccess.isTheme.SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_themes),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isTheme = value)) },
        )

        quickAccess.isLocale.SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_locales),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isLocale = value)) },
        )

        quickAccess.isAvatar.SettingsSwitch(
            title = stringResource(Res.string.quick_access_to_avatar),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isAvatar = value)) },
        )

        quickAccess.isAvatarConnectivityIndicator.SettingsSwitch(
            title = stringResource(Res.string.avatar_connectivity_indicator),
            trueIcon = Icons.Outlined.FlashOn,
            falseIcon = Icons.Filled.FlashOn,
            onCheckedChange = { value -> onQuickAccessChange(quickAccess.copy(isAvatarConnectivityIndicator = value)) },
        )
    }

    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.permission)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        Permission.CAMERA.SettingsSwitch(
            title = stringResource(Res.string.camera),
            trueIcon = Icons.Outlined.CameraAlt,
            falseIcon = Icons.Filled.CameraAlt,
            permissions = permissions,
            coroutineScope = coroutineScope,
            permissionsController = permissionsController,
        )

        Permission.RECORD_AUDIO.SettingsSwitch(
            title = stringResource(Res.string.microphone),
            trueIcon = Icons.Outlined.Mic,
            falseIcon = Icons.Filled.Mic,
            permissions = permissions,
            coroutineScope = coroutineScope,
            permissionsController = permissionsController,
        )

        Permission.LOCATION.SettingsSwitch(
            title = stringResource(Res.string.location),
            trueIcon = Icons.Outlined.LocationOn,
            falseIcon = Icons.Filled.LocationOn,
            permissions = permissions,
            coroutineScope = coroutineScope,
            permissionsController = permissionsController,
        )
    }
    SettingsGroup(
        modifier = Modifier,
        enabled = true,
        title = { Text(text = stringResource(Res.string.recovery)) },
        contentPadding = PaddingValues(16.dp),
    ) {
        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.reset)) },
            subtitle = {
                if (
                    theme != defaultTheme ||
                    density != defaultDensity ||
                    locale != defaultLocale ||
                    quickAccess != defaultQuickAccess
                ) Text(text = stringResource(Res.string.reset))
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Restore, stringResource(Res.string.reset)) },
            onClick = {
                if (theme != defaultTheme) onThemeChange(defaultTheme)
                if (density != defaultDensity) onDensityChange(defaultDensity)
                if (locale != defaultLocale) onLocaleChange(defaultLocale)
                if (quickAccess != defaultQuickAccess) onQuickAccessChange(defaultQuickAccess)
            },
        )
    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()

@Suppress("ComposeParameterOrder")
@Composable
private fun Permission.SettingsSwitch(
    permissions: Set<Permission>,
    coroutineScope: CoroutineScope,
    permissionsController: PermissionsController,
    title: String,
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
) = (this in permissions).SettingsSwitch(
    title,
    modifier,
    enabled,
    trueIcon,
    falseIcon,
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange = { value ->
        if (!value)
            coroutineScope.launch {
                permissionsController.providePermission(this@SettingsSwitch)

                try {
                    permissionsController.providePermission(this@SettingsSwitch)
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
