package data.type.primitives

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import clib.data.type.LGreen
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import compose_app.generated.resources.Res
import compose_app.generated.resources.disabled
import compose_app.generated.resources.enabled
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import org.jetbrains.compose.resources.stringResource

@Composable
public fun Boolean.enabledStringResource(): String =
    stringResource(if (this) Res.string.enabled else Res.string.disabled)

@Suppress("ComposeModifierMissing")
@Composable
public fun Boolean.EnabledText(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    autoSize: TextAutoSize? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
): Unit = Text(
    enabledStringResource(),
    modifier,
    color,
    autoSize,
    fontSize,
    fontStyle,
    fontWeight,
    fontFamily,
    letterSpacing,
    textDecoration,
    textAlign,
    lineHeight,
    overflow,
    softWrap,
    maxLines,
    minLines,
    onTextLayout,
    style,
)

@Suppress("ComposeModifierMissing")
@Composable
public fun Boolean.connectivityStringResource(): String =
    stringResource(if (this) Res.string.online else Res.string.offline)

@Suppress("ComposeModifierMissing")
@Composable
public fun Boolean.ConnectivityText(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    autoSize: TextAutoSize? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
): Unit = Text(
    connectivityStringResource(),
    modifier,
    color,
    autoSize,
    fontSize,
    fontStyle,
    fontWeight,
    fontFamily,
    letterSpacing,
    textDecoration,
    textAlign,
    lineHeight,
    overflow,
    softWrap,
    maxLines,
    minLines,
    onTextLayout,
    style,
)

@Suppress("ComposeParameterOrder")
@Composable
public fun Boolean.ConnectivityIcon(
    onlineModifier: Modifier = Modifier,
    offlineModifier: Modifier = Modifier,
): Unit =
    if (this) Icon(
        Icons.Filled.Circle,
        stringResource(Res.string.online),
        onlineModifier,
        Color.LGreen,
    )
    else Icon(
        Icons.Filled.Circle,
        stringResource(Res.string.offline),
        offlineModifier,
        MaterialTheme.colorScheme.error,
    )

@Suppress("ComposeParameterOrder")
@Composable
public fun Boolean.SettingsSwitch(
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
    onCheckedChange: (Boolean) -> Unit,
): Unit = SettingsSwitch(
    this,
    { Text(title) },
    modifier,
    enabled,
    { Icon(if (this) trueIcon else falseIcon, title) },
    { EnabledText() },
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange = onCheckedChange,
)
