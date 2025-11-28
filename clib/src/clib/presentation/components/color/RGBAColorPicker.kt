package clib.presentation.components.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.color.common.AlphaSlider
import clib.presentation.components.color.common.ColorSlider
import clib.presentation.components.color.common.selectColor
import com.github.skydoves.colorpicker.compose.ColorPickerController

/**
 * A composable function that creates a color picker UI for selecting RGB-A colors. This component
 * contain 3 sliders for adjusting the red, green, and blue values of the color and another slider to adjust the alpha value.
 * By adjusting these values, consumer can select or generate your desired color.
 *
 * @param controller: ColorPickerController.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 * @param redLabel: Red label.
 * @param greenLabel: Green label.
 * @param blueLabel: Blue label.
 *
 * @return @Composable: A color picker UI for selecting RGB-A colors.
 */
@Composable
internal fun RGBAColorPicker(
    controller: ColorPickerController,
    modifier: Modifier = Modifier,
    title: String = "Select color rgba",
    redLabel: String = "Red",
    greenLabel: String = "Green",
    blueLabel: String = "Blue",
): Unit = Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Row {
            // Sliders for adjusting RGB-A values
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                ColorSlider(
                    redLabel,
                    Color.Red,
                    controller.selectedColor.value.red,
                ) { value ->
                    controller.selectColor(controller.selectedColor.value.copy(red = value))
                }
                ColorSlider(
                    greenLabel,
                    Color.Green,
                    controller.selectedColor.value.green,
                ) { value ->
                    controller.selectColor(controller.selectedColor.value.copy(green = value))
                }
                ColorSlider(
                    blueLabel,
                    Color.Blue,
                    controller.selectedColor.value.blue,
                ) { value ->
                    controller.selectColor(controller.selectedColor.value.copy(blue = value))
                }
                AlphaSlider(controller)
            }
        }
    }
}
