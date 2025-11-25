package clib.presentation.color.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

public data class ColorPicker(
    val title: String = "Pick a color",
    val debounceDuration: Long = 200L,
    val useAlphaSlider: Boolean = true,
    val useHsvWheel: Boolean = true,
    val useBrightnessSlider: Boolean = true,
    val useHexField: Boolean = true,
    val useRgbFields: Boolean = false,
    val useAlphaTile: Boolean = false,
    val showRecentColors: Boolean = true,
    val recentColorsLabel: String = "Recent",
    val recentColorsLimit: Int = 6,
    val confirmText: String = "Confirm",
    val cancelText: String = "Cancel",
    val redLabel: String = "R",
    val greenLabel: String = "G",
    val blueLabel: String = "B",
    val hexLabel: String = "Hex",
    val dialogShape: Shape = RoundedCornerShape(24.dp),
    val dialogPadding: PaddingValues = PaddingValues(24.dp)
)
