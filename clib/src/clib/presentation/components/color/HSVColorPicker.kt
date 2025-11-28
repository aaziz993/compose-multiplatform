package clib.presentation.components.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/**
 * A composable function that creates a color picker UI for selecting HSV properties to get color. This component
 * contain one color spectrum, that consumer can drag and select their color. Using the other three sliders, consumer can change the
 * selected color's saturation, lightness and alpha values.
 * By adjusting these values, consumer can select or generate their desired color.
 *
 * @param controller: ColorPickerController.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A color picker UI for selecting HSV colors.
 */
@Composable
internal fun HSVColorPicker(
    controller: ColorPickerController,
    modifier: Modifier = Modifier,
    title: String = "Select color hsv",
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    controller = controller,
                )

                Spacer(Modifier.height(12.dp))

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    controller = controller,
                )

                Spacer(Modifier.height(12.dp))

                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    controller = controller,
                )
            }
        }
    }
}
