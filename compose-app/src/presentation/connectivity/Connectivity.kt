package presentation.connectivity

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.SignalCellular0Bar
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet4Bar
import androidx.compose.material.icons.outlined.SignalCellular0Bar
import androidx.compose.material.icons.outlined.SignalCellularConnectedNoInternet4Bar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import clib.data.type.LGreen
import compose_app.generated.resources.Res
import compose_app.generated.resources.connectivity_indicator_text
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import dev.jordond.connectivity.Connectivity.Status
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.stringResource(): String =
    stringResource(if (isConnected) Res.string.online else Res.string.offline)

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.Text(
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
    stringResource(),
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

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.filledImageVector(): ImageVector =
    if (isConnected) Icons.Filled.SignalCellular0Bar
    else Icons.Filled.SignalCellularConnectedNoInternet4Bar


@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.outlinedImageVector(): ImageVector =
    if (isConnected) Icons.Outlined.SignalCellular0Bar
    else Icons.Outlined.SignalCellularConnectedNoInternet4Bar

@Suppress("ComposeUnstableReceiver", "ComposeModifierMissing")
@Composable
public fun Status.DefaultIcon(): Unit =
    Icon(
        if (isConnected) Icons.Default.SignalCellular0Bar
        else Icons.Default.SignalCellularConnectedNoInternet0Bar,
        stringResource(Res.string.connectivity_indicator_text),
    )

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.CircleIcon(
    onlineModifier: Modifier = Modifier,
    offlineModifier: Modifier = Modifier,
): Unit =
    if (isConnected) Icon(
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


