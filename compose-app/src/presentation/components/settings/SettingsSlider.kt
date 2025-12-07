package presentation.components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.settings.SettingsSlider
import clib.presentation.components.slider.MaterialSliderColors
import clib.presentation.components.slider.MaterialSliderDefaults
import clib.presentation.components.slider.SliderBrushColor
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import klib.data.type.primitives.number.decimal.formatter.DecimalFormatter
import kotlin.math.roundToInt
import pro.respawn.kmmutils.common.signChar

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsSliderFinished(
    title: String,
    initialValue: Float,
    modifier: Modifier = Modifier,
    subtitle: @Composable ((value: Float) -> Unit)? = { value ->
        Text("${if (value < 0) value.signChar else ""}${DecimalFormatter.DefaultFormatter.format((value * 100).roundToInt()).displayValue}")
    },
    icon: ImageVector,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    trackHeight: Dp = 4.dp,
    thumbRadius: Dp = 10.dp,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    sliderColors: MaterialSliderColors = MaterialSliderDefaults.defaultColors(
        thumbColor = SliderBrushColor(
            color = colors.actionColor(enabled),
        ),
        activeTrackColor = SliderBrushColor(
            color = colors.actionColor(enabled),
        ),
        inactiveTrackColor = SliderBrushColor(
            color = colors.actionColor(enabled).copy(alpha = 0.12f),
        ),
    ),
    borderStroke: BorderStroke? = null,
    drawInactiveTrack: Boolean = true,
    coerceThumbInTrack: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    onValueChange: (Float) -> Unit,
) {
    var value by remember { mutableFloatStateOf(initialValue) }
    SettingsSlider(
        { Text(title) },
        value,
        modifier,
        {
            subtitle?.invoke(value)
        },
        { Icon(icon, title) },
        enabled,
        valueRange,
        steps,
        { onValueChange(value) },
        trackHeight,
        thumbRadius,
        colors,
        sliderColors,
        borderStroke,
        drawInactiveTrack,
        coerceThumbInTrack,
        interactionSource,
        tonalElevation,
        shadowElevation,
    ) { it, _ -> value = it }
}
