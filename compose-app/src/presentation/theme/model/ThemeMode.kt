package presentation.theme.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose_app.generated.resources.Res
import compose_app.generated.resources.dark
import compose_app.generated.resources.light
import compose_app.generated.resources.system
import compose_app.generated.resources.adaptive
import clib.data.type.primitives.string.stringResource
import clib.presentation.theme.model.ThemeMode

@Composable
public fun ThemeMode.isDarkStringResource(): String = when (this) {
    ThemeMode.SYSTEM -> stringResource(Res.string.system)
    ThemeMode.LIGHT -> stringResource(Res.string.light)
    ThemeMode.DARK -> stringResource(Res.string.dark)
    ThemeMode.ADAPTIVE -> stringResource(Res.string.adaptive)
}

public fun ThemeMode.isDarkIcon(): ImageVector = when (this) {
    ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
    ThemeMode.LIGHT -> Icons.Default.LightMode
    ThemeMode.DARK -> Icons.Default.DarkMode
    ThemeMode.ADAPTIVE -> Icons.Default.AutoMode
}

@Suppress("ComposeModifierMissing")
@Composable
public fun ThemeMode.IsDarkIcon(): Unit = Icon(isDarkIcon(), isDarkStringResource())


