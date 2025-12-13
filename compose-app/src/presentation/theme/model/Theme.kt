package presentation.theme.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.dark_theme
import compose_app.generated.resources.light_theme
import compose_app.generated.resources.system_theme
import clib.data.type.primitives.string.stringResource

@Composable
public fun Theme.isDarkStringResource(): String = when (isDark) {
    null -> stringResource(Res.string.system_theme)
    false -> stringResource(Res.string.light_theme)
    true -> stringResource(Res.string.dark_theme)
}

public fun Theme.isDarkIcon(): ImageVector = when (isDark) {
    null -> Icons.Default.SettingsBrightness
    false -> Icons.Default.LightMode
    true -> Icons.Default.DarkMode
}

@Suppress("ComposeModifierMissing")
@Composable
public fun Theme.IsDarkIcon(): Unit = Icon(isDarkIcon(), isDarkStringResource())


