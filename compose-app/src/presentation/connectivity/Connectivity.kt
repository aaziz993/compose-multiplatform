package presentation.connectivity

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import compose_app.generated.resources.offline
import compose_app.generated.resources.online
import dev.jordond.connectivity.Connectivity.Status
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.connectivityStringResource(): String =
    stringResource(if (isConnected) Res.string.online else Res.string.offline)

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.ConnectivityText(
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

@Suppress("ComposeUnstableReceiver")
@Composable
public fun Status.ConnectivityCircleIcon(
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


