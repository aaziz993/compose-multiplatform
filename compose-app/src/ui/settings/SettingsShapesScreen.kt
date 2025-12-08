package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.presentation.theme.model.Theme
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.bottom_end
import compose_app.generated.resources.bottom_start
import compose_app.generated.resources.density
import compose_app.generated.resources.extra_large
import compose_app.generated.resources.extra_small
import compose_app.generated.resources.large
import compose_app.generated.resources.medium
import compose_app.generated.resources.small
import compose_app.generated.resources.top_end
import compose_app.generated.resources.top_start
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsListPickerDialog
import presentation.components.settings.SettingsSliderFinished
import ui.navigation.presentation.SettingsShapes

@Composable
public fun SettingsShapesScreen(
    modifier: Modifier = Modifier,
    route: SettingsShapes = SettingsShapes,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val cornerSizes = remember { listOf("dp", "px", "percent") }

    SettingsCornerBasedShape(
        stringResource(Res.string.extra_small),
        cornerSizes,
        theme.shapes.extraSmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                shapes = theme.shapes.copy(extraSmall = value),
            ),
        )
    }

    SettingsCornerBasedShape(
        stringResource(Res.string.small),
        cornerSizes,
        theme.shapes.small,
    ) { value ->
        onThemeChange(
            theme.copy(
                shapes = theme.shapes.copy(small = value),
            ),
        )
    }

    SettingsCornerBasedShape(
        stringResource(Res.string.medium),
        cornerSizes,
        theme.shapes.medium,
    ) { value ->
        onThemeChange(
            theme.copy(
                shapes = theme.shapes.copy(medium = value),
            ),
        )
    }

    SettingsCornerBasedShape(
        stringResource(Res.string.large),
        cornerSizes,
        theme.shapes.large,
    ) { value ->
        onThemeChange(
            theme.copy(
                shapes = theme.shapes.copy(large = value),
            ),
        )
    }

    SettingsCornerBasedShape(
        stringResource(Res.string.extra_large),
        cornerSizes,
        theme.shapes.extraLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                shapes = theme.shapes.copy(extraLarge = value),
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsShapesScreen(): Unit = SettingsShapesScreen()

@Composable
private fun SettingsCornerBasedShape(
    title: String,
    cornerSizes: List<String>,
    value: CornerBasedShape,
    onValueChange: (CornerBasedShape) -> Unit,
) = SettingsGroup(
    modifier = Modifier,
    enabled = true,
    title = { Text(title) },
    contentPadding = PaddingValues(16.dp),
) {
    SettingsCornerSize(
        stringResource(Res.string.top_start),
        cornerSizes,
        value.topStart,
    ) {
        onValueChange(value.copy(topStart = it))
    }

    SettingsCornerSize(
        stringResource(Res.string.top_end),
        cornerSizes,
        value.topEnd,
    ) {
        onValueChange(value.copy(topEnd = it))
    }

    SettingsCornerSize(
        stringResource(Res.string.bottom_end),
        cornerSizes,
        value.bottomEnd,
    ) {
        onValueChange(value.copy(bottomEnd = it))
    }

    SettingsCornerSize(
        stringResource(Res.string.bottom_start),
        cornerSizes,
        value.bottomStart,
    ) {
        onValueChange(value.copy(bottomStart = it))
    }
}

@Composable
private fun SettingsCornerSize(
    title: String,
    cornerSizes: List<String>,
    value: CornerSize,
    onValueChange: (CornerSize) -> Unit,
) {
    var cornerSize by remember { mutableStateOf("dp") }

    SettingsListPickerDialog(
        cornerSize,
        values = cornerSizes,
        title = title,
        icon = Icons.Default.RoundedCorner,
        modifier = Modifier,
        enabled = true,
    ) { value ->
        cornerSize = value
        false
    }

    SettingsSliderFinished(
        title = stringResource(Res.string.density),
        initialValue = value.toPx(Size(50), LocalDensity.current),
        icon = { Icons.Default.RoundedCorner },
        enabled = true,
        valueRange = 0f..50f,
        onValueChanged = { value ->
            onValueChange(CornerSize(value))
        },
    )
}
