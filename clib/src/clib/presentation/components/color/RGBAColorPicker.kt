package clib.presentation.components.color

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
import clib.presentation.components.color.common.ColorSlider
import clib.presentation.components.color.common.SelectedColorDetail

/**
 * A composable function that creates a color picker UI for selecting RGB-A colors. This component
 * contain 3 sliders for adjusting the red, green, and blue values of the color and another slider to adjust the alpha value.
 * By adjusting these values, consumer can select or generate your desired color.
 *
 * @param value Color value.
 * @param onValueChange Callback on color value change.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 * @param red: Red label.
 * @param green: Green label.
 * @param blue: Blue label.
 * @param alpha: Alpha label.
 *
 * @return @Composable: A color picker UI for selecting RGB-A colors.
 */
@Composable
internal fun RGBAColorPicker(
    value: Color,
    onValueChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select color rgba",
    red: String = "Red",
    green: String = "Green",
    blue: String = "Blue",
    alpha: String = "Alpha",
    hex: String = "Hex",
    copy: String = "Copy",
): Unit = Column(
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

    Row(
        modifier = Modifier.weight(.8f),
    ) {
        // Sliders for adjusting RGB-A values
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            ColorSlider(
                red,
                Color.Red,
                255,
                value.red,
            ) {
                onValueChange(value.copy(red = it))
            }
            ColorSlider(
                green,
                Color.Green,
                255,
                value.green,
            ) {
                onValueChange(value.copy(green = it))
            }
            ColorSlider(
                blue,
                Color.Blue,
                255,
                value.blue,
            ) {
                onValueChange(value.copy(blue = it))
            }
            ColorSlider(
                blue,
                value,
                255,
                value.alpha,
            ) {
                onValueChange(value.copy(alpha = it))
            }
        }
    }

    SelectedColorDetail(
        value,
        onValueChange,
        Modifier.weight(.2f),
        hex,
        copy,
    )
}
