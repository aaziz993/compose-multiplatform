package presentation.components.tooltipbox

import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider

@Suppress("ComposeModifierMissing", "ComposeParameterOrder")
@Composable
public fun PlainTooltipBox(
    positionProvider: PopupPositionProvider = TooltipDefaults.rememberTooltipPositionProvider(
        TooltipAnchorPosition.Above,
    ),
    tooltip: String,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    focusable: Boolean = false,
    enableUserUnput: Boolean = true,
    hasAction: Boolean = false,
    content: @Composable () -> Unit
): Unit = TooltipBox(
    positionProvider,
    { PlainTooltip { Text(tooltip) } },
    rememberTooltipState(),
    modifier,
    onDismissRequest,
    focusable,
    enableUserUnput,
    hasAction,
    content = content,
)
