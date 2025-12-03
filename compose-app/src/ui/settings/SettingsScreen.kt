package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import arrow.core.left
import arrow.core.right
import arrow.optics.copy
import clib.data.location.country.getEmojiFlag
import clib.data.permission.BindEffect
import clib.data.permission.rememberPermissions
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.components.color.ColorPickerBottomSheet
import clib.presentation.components.color.model.ColorPicker
import clib.presentation.components.settings.SettingsSlider
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import clib.presentation.navigation.NavigationAction
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import compose_app.generated.resources.Res
import compose_app.generated.resources.alpha
import compose_app.generated.resources.appearance
import compose_app.generated.resources.blend
import compose_app.generated.resources.blue
import compose_app.generated.resources.brightness
import compose_app.generated.resources.camera
import compose_app.generated.resources.close
import compose_app.generated.resources.color_palette
import compose_app.generated.resources.confirm
import compose_app.generated.resources.density
import compose_app.generated.resources.dynamic_color_palette
import compose_app.generated.resources.expressive
import compose_app.generated.resources.font_scale
import compose_app.generated.resources.green
import compose_app.generated.resources.grid
import compose_app.generated.resources.hex
import compose_app.generated.resources.copy
import compose_app.generated.resources.left
import compose_app.generated.resources.right
import compose_app.generated.resources.high_contrast
import compose_app.generated.resources.hsla
import compose_app.generated.resources.hsv
import compose_app.generated.resources.language
import compose_app.generated.resources.lightness
import compose_app.generated.resources.location
import compose_app.generated.resources.microphone
import compose_app.generated.resources.permissions
import compose_app.generated.resources.pick_color
import compose_app.generated.resources.quick_access_to_avatar
import compose_app.generated.resources.quick_access_to_locales
import compose_app.generated.resources.quick_access_to_support
import compose_app.generated.resources.quick_access_to_themes
import compose_app.generated.resources.red
import compose_app.generated.resources.reset
import compose_app.generated.resources.rgba
import compose_app.generated.resources.saturation
import compose_app.generated.resources.theme
import data.type.primitives.EnabledText
import data.type.primitives.string.asStringResource
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.model.Permission
import klib.data.type.auth.model.Auth
import kotlin.String
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import presentation.components.country.LocalePickerDialog
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
    locale: Locale = Locale.current,
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

        SettingsSwitch(
            state = theme.isDynamic,
            title = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            subtitle = {
                theme.isDynamic.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Palette, stringResource(Res.string.dynamic_color_palette)) },
            onCheckedChange = { state ->
                onThemeChange(theme.copy(isDynamic = !theme.isDynamic))
            },
        )

        SettingsSwitch(
            state = theme.isHighContrast,
            title = { Text(text = stringResource(Res.string.high_contrast)) },
            subtitle = {
                theme.isHighContrast.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Accessibility, stringResource(Res.string.high_contrast)) },
            onCheckedChange = { state ->
                onThemeChange(theme.copy(isHighContrast = !theme.isHighContrast))
            },
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

        var showSheet by remember { mutableStateOf(false) }

        if (showSheet)
            ColorPickerBottomSheet(
                { showSheet = false },
                {
                    showSheet = false
                },
                picker = ColorPicker(
                    stringResource(Res.string.pick_color),
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
                    stringResource(Res.string.close),
                    stringResource(Res.string.confirm),
                ),
            )

        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            subtitle = { Text(text = stringResource(Res.string.dynamic_color_palette)) },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.ColorLens, stringResource(Res.string.dynamic_color_palette)) },
            onClick = {
                showSheet = true
            },
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

        var isLocalePickerDialogOpen by remember { mutableStateOf(false) }

        if (isLocalePickerDialogOpen)
            LocalePickerDialog(
                locales,
                onLocaleChange,
            ) {
                isLocalePickerDialogOpen = false
            }

        SettingsMenuLink(
            title = { Text(text = stringResource(Res.string.language)) },
            subtitle = {
                Text(text = locale.toString().asStringResource())
            },
            modifier = Modifier,
            enabled = true,
            icon = {
                Text(locale.country()!!.alpha2.getEmojiFlag())
            },
            onClick = {
                isLocalePickerDialogOpen = true
            },
        )

        SettingsSwitch(
            state = quickAccess.isSupport,
            title = { Text(text = stringResource(Res.string.quick_access_to_support)) },
            subtitle = {
                quickAccess.isSupport.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.FlashOn, stringResource(Res.string.quick_access_to_support)) },
            onCheckedChange = { state ->
                onQuickAccessChange(quickAccess.copy(isSupport = !quickAccess.isSupport))
            },
        )

        SettingsSwitch(
            state = quickAccess.isTheme,
            title = { Text(text = stringResource(Res.string.quick_access_to_themes)) },
            subtitle = {
                quickAccess.isTheme.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.FlashOn, stringResource(Res.string.quick_access_to_themes)) },
            onCheckedChange = { state ->
                onQuickAccessChange(quickAccess.copy(isTheme = !quickAccess.isTheme))
            },
        )

        SettingsSwitch(
            state = quickAccess.isLocale,
            title = { Text(text = stringResource(Res.string.quick_access_to_locales)) },
            subtitle = {
                quickAccess.isLocale.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.FlashOn, stringResource(Res.string.quick_access_to_locales)) },
            onCheckedChange = { state ->
                onQuickAccessChange(quickAccess.copy(isLocale = !quickAccess.isLocale))
            },
        )

        SettingsSwitch(
            state = quickAccess.isAvatar,
            title = { Text(text = stringResource(Res.string.quick_access_to_avatar)) },
            subtitle = {
                quickAccess.isAvatar.EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.FlashOn, stringResource(Res.string.quick_access_to_avatar)) },
            onCheckedChange = { state ->
                onQuickAccessChange(quickAccess.copy(isAvatar = !quickAccess.isAvatar))
            },
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
            subtitle = {
                (Permission.CAMERA in permissions).EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.CameraAlt, stringResource(Res.string.camera)) },
            onCheckedChange = { state: Boolean ->
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
            subtitle = {
                (Permission.RECORD_AUDIO in permissions).EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.Mic, stringResource(Res.string.microphone)) },
            onCheckedChange = { state: Boolean ->
                coroutineScope.launch {
                    permissionsController.providePermission(Permission.RECORD_AUDIO)
                }
            },
        )

        SettingsSwitch(
            state = Permission.LOCATION in permissions,
            title = { Text(text = stringResource(Res.string.location)) },
            subtitle = {
                (Permission.LOCATION in permissions).EnabledText()
            },
            modifier = Modifier,
            enabled = true,
            icon = { Icon(Icons.Default.LocationOn, stringResource(Res.string.location)) },
            onCheckedChange = { state: Boolean ->
                coroutineScope.launch {
                    permissionsController.providePermission(Permission.LOCATION)
                }
            },
        )

        Button(
            onClick = {
                onThemeChange(defaultTheme)
                onDensityChange(defaultDensity)
                onLocaleChange(defaultLocale)
                onQuickAccessChange(defaultQuickAccess)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.reset))
        }
    }
}

@Preview
@Composable
public fun PreviewSettingsScreen(): Unit = SettingsScreen()


