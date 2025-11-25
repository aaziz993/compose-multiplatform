package clib.presentation.color.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public data class ColorView(
    val showLabel: Boolean = true,
    val showHexCode: Boolean = true,
    val showPreviewCircle: Boolean = true,
    val showDivider: Boolean = true,
    val circleSize: Dp = 22.dp,
    val circleShape: Shape = CircleShape,
    val rowPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    val spacing: Dp = 12.dp,
)
