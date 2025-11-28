package clib.presentation.components.color

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.getColorMap
import clib.presentation.components.color.common.ColorColumn
import clib.presentation.components.color.common.selectColor
import com.github.skydoves.colorpicker.compose.ColorPickerController

/**
 * A composable function that creates a grid to select colors. This color grid is created with
 * 16 predefined major colors and those color's 10 color variances.
 *
 * @param controller: ColorPickerController.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A grid UI to select colors.
 */
@Suppress("UnusedBoxWithConstraintsScope")
@Composable
internal fun GridColorPicker(
    controller: ColorPickerController,
    modifier: Modifier = Modifier,
    title: String = "Select color grid",
): Unit = Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Color.getColorMap().keys.map { key ->
                    ColorColumn(
                        boxSize = boxSize,
                        givenColor = key,
                        value = controller.selectedColor.value,
                        onValueChange = { value ->
                            controller.selectColor(value)
                        },
                    )
                }
            }
        }
    }
}
