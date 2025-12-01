package clib.presentation.components.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.toColor
import clib.presentation.components.color.common.SliderHue
import clib.presentation.components.slider.ColorfulSlider
import clib.presentation.components.slider.MaterialSliderDefaults
import clib.presentation.components.slider.SliderBrushColor
import com.materialkolor.ktx.blend
import com.materialkolor.ktx.toHex

/**
 * A composable function that creates a color picker UI to select color by selecting two colors and blend.
 * This component contains two color spectrum(s) to select first and second color to blend and one slider, that consumer
 * can change how blended color bias to first or second color.
 * By selecting first and second color then by adjusting color bias value, consumer can select or generate their desired color.
 *
 * @param value Color value.
 * @param onValueChange Callback on color value change.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A color picker UI for selecting blended color.
 */
@Composable
internal fun BlendColorPicker(
    hexColorChanged: Boolean,
    value: Color,
    onValueChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select color blend",
) = Column(
    modifier = Modifier
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(8.dp),
        )
        .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
        .then(modifier),
) {
    Text(
        text = title,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
        style = MaterialTheme.typography.bodySmall,
        fontSize = 12.sp,
    )

    // State variables for first color hue and second color hue
    var firstHue by remember(hexColorChanged) {
        mutableFloatStateOf(value.toColor().toHSL().h.takeUnless(Float::isNaN) ?: 0f)
    }
    var secondHue by remember(hexColorChanged) {
        mutableFloatStateOf(value.toColor().toHSL().h.takeUnless(Float::isNaN) ?: 0f)
    }

    var firstBlendColor by remember(hexColorChanged) { mutableStateOf(value) }
    var secondBlendColor by remember(hexColorChanged) { mutableStateOf(value) }
    var colorBias by remember(hexColorChanged) { mutableFloatStateOf(.5f) }

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.weight(.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SliderHue(
                Modifier.padding(top = 4.dp, bottom = 4.dp),
                firstHue,
            ) {
                firstHue = it
                firstBlendColor = Color.hsv(it, 1f, 1f)
                onValueChange(firstBlendColor.blend(secondBlendColor, colorBias))
            }

            Text(text = firstBlendColor.toHex())
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SliderHue(
                Modifier.padding(top = 4.dp, bottom = 4.dp),
                secondHue,
            ) {
                secondHue = it
                secondBlendColor = Color.hsv(it, 1f, 1f)
                onValueChange(firstBlendColor.blend(secondBlendColor, colorBias))
            }

            Text(text = secondBlendColor.toHex())
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .weight(.2f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .background(
                        firstBlendColor,
                        shape = MaterialTheme.shapes.large,
                    )
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(12.dp),
                    ),
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Row(
            modifier = Modifier.weight(.7f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                contentDescription = "Left arrow",
                Modifier
                    .weight(.25f)
                    .size(40.dp),
            )

            ColorfulSlider(
                value = colorBias,
                onValueChange = {
                    colorBias = it
                    onValueChange(firstBlendColor.blend(secondBlendColor, colorBias))
                },
                modifier = Modifier
                    .height(50.dp)
                    .weight(.7f),

                valueRange = 0f..1f,
                colors = MaterialSliderDefaults.defaultColors(
                    thumbColor = SliderBrushColor(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    activeTrackColor = SliderBrushColor(
                        color = value,
                    ),
                ),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "Right arrow",
                Modifier
                    .weight(.25f)
                    .size(40.dp),
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier
                .weight(.2f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .background(
                        secondBlendColor,
                        shape = MaterialTheme.shapes.large,
                    )
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(12.dp),
                    ),
            )
        }
    }
}
