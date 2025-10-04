package presentation.components.tooltipbox

import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable

@Suppress("ComposeModifierMissing")
@Composable
public fun AppTooltipBox(tooltip: String, content: @Composable () -> Unit): Unit = TooltipBox(
    positionProvider =
        TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
        ),
    tooltip = { PlainTooltip { Text(tooltip) } },
    state = rememberTooltipState(),
    content = content,
)
