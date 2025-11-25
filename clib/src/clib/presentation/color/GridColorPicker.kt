package clib.presentation.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import clib.presentation.color.common.ColorColum

/**
 * A composable function that creates a grid to select colors. This color grid is created with
 * 16 predefined major colors and those color's 10 color variances.
 *
 * @param modifier: Modifier: The modifier to apply to this layout.
 * @param lastSelectedColor: Color: variable to pass last selected color.
 * @param onColorSelected: (selectedColor: Color) -> Unit: Callback to invoke when a color is selected.
 *
 * @return @Composable: A grid UI to select colors.
 */
@Suppress("UnusedBoxWithConstraintsScope")
@Composable
public fun GridColorPicker(
    modifier: Modifier = Modifier,
    title: String = "Select color grid",
    lastSelectedColor: Color = Color.White,
    onColorSelected: (selectedColor: Color) -> Unit
) {

    var selectedColor by remember { mutableStateOf(lastSelectedColor) }

    val onSelectColor: (color: Color) -> Unit = {
        selectedColor = it
    }

    // Launch an effect to invoke the provided callback with the selected color
    LaunchedEffect(selectedColor) {
        onColorSelected.invoke(selectedColor)
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
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
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

            BoxWithConstraints {
                val screenWidth = maxWidth
                val boxSize = screenWidth * .065f

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 4.dp, end = 4.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = Color.Red,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.Rose,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LPurple,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.DPurple,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.DBlue,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LBlue,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LLBlue,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LCyan,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.DCyan,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.DGreen,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LGreen,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.LLGreen,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.Yellow,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.Gold,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                    ColorColum(
                        boxSize = boxSize,
                        givenColor = clib.data.type.color.Color.Orange,
                        selectedColor = selectedColor,
                        onSelect = onSelectColor,
                    )
                }
            }
        }
    }
}
