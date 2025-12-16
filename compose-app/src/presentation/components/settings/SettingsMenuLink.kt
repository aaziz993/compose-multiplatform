package presentation.components.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsMenuLink(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: ImageVector? = null,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    onClick: () -> Unit,
): Unit = SettingsMenuLink(
    {
        Text(
            text = title,
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )
    },
    modifier,
    enabled,
    icon?.let { { Icon(it, subtitle ?: title) } },
    subtitle?.let { { Text(it) } },
    action,
    colors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onClick,
)
