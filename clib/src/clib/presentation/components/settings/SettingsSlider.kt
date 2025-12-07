package clib.presentation.components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.slider.ColorfulSlider
import clib.presentation.components.slider.MaterialSliderColors
import clib.presentation.components.slider.MaterialSliderDefaults
import clib.presentation.components.slider.SliderBrushColor
import clib.presentation.components.slider.ThumbRadius
import clib.presentation.components.slider.TrackHeight
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import com.alorma.compose.settings.ui.base.internal.SettingsTileScaffold
import klib.data.type.primitives.number.decimal.formatter.DecimalFormatter
import kotlin.math.roundToInt
import pro.respawn.kmmutils.common.signChar

@Composable
public fun SettingsSlider(
    title: @Composable () -> Unit,
    value: Float,
    modifier: Modifier = Modifier,
    subtitle: @Composable (() -> Unit)? = {
        Text("${if (value < 0) value.signChar else ""}${DecimalFormatter.DefaultFormatter.format((value * 100).roundToInt()).displayValue}")
    },
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    trackHeight: Dp = TrackHeight,
    thumbRadius: Dp = ThumbRadius,
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
    onValueChange: (Float, Offset) -> Unit,
) {
    SettingsTileScaffold(
        modifier = modifier,
        enabled = enabled,
        title = title,
        subtitle = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                subtitle?.invoke()
                ColorfulSlider(
                    value,
                    onValueChange,
                    Modifier
                        .requiredHeight(thumbRadius * 2)
                        .padding(end = 16.dp),
                    enabled,
                    valueRange,
                    steps,
                    onValueChangeFinished,
                    trackHeight,
                    thumbRadius,
                    sliderColors,
                    borderStroke,
                    drawInactiveTrack,
                    coerceThumbInTrack,
                    interactionSource,
                )
            }
        },
        icon = icon,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    )
}
