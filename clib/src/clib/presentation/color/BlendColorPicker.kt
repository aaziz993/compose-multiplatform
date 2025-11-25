package clib.presentation.color

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
import androidx.compose.material3.Slider
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
import clib.data.type.color.toColor
import clib.presentation.color.common.SliderHue
import com.materialkolor.ktx.blend
import com.materialkolor.ktx.toHex

/**
 * A composable function that creates a color picker UI to select color by selecting two colors and blend.
 * This component contains two color spectrum(s) to select first and second color to blend and one slider, that consumer
 * can change how blended color bias to first or second color.
 * By selecting first and second color then by adjusting color bias value, consumer can select or generate their desired color.
 *
 * @param modifier: Modifier: The modifier to apply to this layout.
 * @param onColorSelected: (selectedColor: Color) -> Unit: Callback to invoke when a color is selected.
 *
 * @return @Composable: A color picker UI for selecting blended color.
 */
@Composable
public fun BlendColorPicker(
    modifier: Modifier = Modifier,
    title: String = "Select color blend",
    onColorSelected: (selectedColor: Color) -> Unit
) {
    val whiteHsl = Color.White.toColor().toHSL()
    // State variables for first color hue and second color hue
    val firstHue = remember { mutableFloatStateOf(whiteHsl.h) }
    val secondHue = remember { mutableFloatStateOf(whiteHsl.h) }

    val firstBlendColor = remember { mutableStateOf(Color.White) }
    val firstBlendColorHex = remember { mutableStateOf(Color.White.toHex()) }
    val secondBlendColor = remember { mutableStateOf(Color.White) }
    val secondBlendColorHex = remember { mutableStateOf(Color.White.toHex()) }

    var colorBias by remember { mutableFloatStateOf(.5f) }

    // Derived state for the color based on blending selected first and second color
    val color by remember {
        derivedStateOf {
            firstBlendColor.value.blend(secondBlendColor.value, colorBias)
        }
    }

    // Launch an effect to invoke the provided callback with the selected color
    LaunchedEffect(color) {
        onColorSelected.invoke(color)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(
            modifier = Modifier
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                )
                .background(Color.White)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
        ) {

            Text(
                text = title,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
            )

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
                        onColorSelect = { selectedColor ->
                            firstHue.floatValue = selectedColor.toColor().toHSL().h
                            firstBlendColor.value = selectedColor
                            firstBlendColorHex.value = selectedColor.toHex()
                        },
                    )

                    Text(text = firstBlendColorHex.value)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    SliderHue(
                        Modifier.padding(top = 4.dp, bottom = 4.dp),
                        onColorSelect = { selectedColor ->
                            secondHue.floatValue = selectedColor.toColor().toHSL().h
                            secondBlendColor.value = selectedColor
                            secondBlendColorHex.value = selectedColor.toHex()
                        },
                    )

                    Text(text = secondBlendColorHex.value)
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
                                firstBlendColor.value,
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

                    Slider(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(.7f),
                        value = colorBias,
                        valueRange = 0f..1f,
                        onValueChange = {
                            colorBias = it
                        },
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
                                secondBlendColor.value,
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
    }
}
