package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontStyle.Companion.Normal
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.text.font.FontSynthesis.Companion.None
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.theme.density.toFloatPx
import clib.presentation.theme.model.Theme
import clib.presentation.theme.typography.FontFamilySerial
import clib.presentation.theme.typography.FontStyleSerial
import clib.presentation.theme.typography.FontSynthesisSerial
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.display_large
import compose_app.generated.resources.display_medium
import compose_app.generated.resources.display_small
import compose_app.generated.resources.headline_large
import compose_app.generated.resources.headline_medium
import compose_app.generated.resources.headline_small
import compose_app.generated.resources.title_large
import compose_app.generated.resources.title_medium
import compose_app.generated.resources.title_small
import compose_app.generated.resources.body_large
import compose_app.generated.resources.body_medium
import compose_app.generated.resources.body_small
import compose_app.generated.resources.label_large
import compose_app.generated.resources.label_medium
import compose_app.generated.resources.label_small
import compose_app.generated.resources.display_large_emphasized
import compose_app.generated.resources.display_medium_emphasized
import compose_app.generated.resources.display_small_emphasized
import compose_app.generated.resources.headline_large_emphasized
import compose_app.generated.resources.headline_medium_emphasized
import compose_app.generated.resources.headline_small_emphasized
import compose_app.generated.resources.title_large_emphasized
import compose_app.generated.resources.title_medium_emphasized
import compose_app.generated.resources.title_small_emphasized
import compose_app.generated.resources.body_large_emphasized
import compose_app.generated.resources.body_medium_emphasized
import compose_app.generated.resources.body_small_emphasized
import compose_app.generated.resources.font_family
import compose_app.generated.resources.label_large_emphasized
import compose_app.generated.resources.label_medium_emphasized
import compose_app.generated.resources.label_small_emphasized
import compose_app.generated.resources.font_size
import compose_app.generated.resources.font_style
import compose_app.generated.resources.font_synthesis
import compose_app.generated.resources.font_weight
import compose_app.generated.resources.letter_spacing
import compose_app.generated.resources.line_height
import klib.data.type.serialization.coders.tree.deserialize
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsListPickerDialog
import presentation.components.settings.SettingsSliderFinished
import ui.navigation.presentation.SettingsTypography

@Composable
public fun SettingsTypographyScreen(
    modifier: Modifier = Modifier,
    route: SettingsTypography = SettingsTypography,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {

    val fontStyles = remember { listOf(Normal, Italic) }

    val fontSynthesis = remember { listOf(None, Weight, Style, All) }

    val fontFamilies = remember {
        listOf<FontFamilySerial>(
            FontFamily.Default,
            FontFamily.SansSerif,
            FontFamily.Serif,
            FontFamily.Monospace,
            FontFamily.Cursive,
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_large),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displayLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displayLarge = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_medium),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displayMedium,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displayMedium = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_small),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displaySmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displaySmall = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_large),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineLarge = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_medium),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineMedium,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineMedium = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_small),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineSmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineSmall = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_large),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleLarge = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_medium),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleMedium,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleMedium = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_small),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleSmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleSmall = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_large),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodyLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodyLarge = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_medium),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodyMedium,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodyMedium = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_small),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodySmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodySmall = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_large),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelLarge,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelLarge = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_medium),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelMedium,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelMedium = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_small),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelSmall,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelSmall = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_large_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displayLargeEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displayLargeEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_medium_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displayMediumEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displayMediumEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.display_small_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.displaySmallEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(displaySmallEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_large_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineLargeEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineLargeEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_medium_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineMediumEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineMediumEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.headline_small_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.headlineSmallEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(headlineSmallEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_large_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleLargeEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleLargeEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_medium_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleMediumEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleMediumEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.title_small_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.titleSmallEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(titleSmallEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_large_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodyLargeEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodyLargeEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_medium_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodyMediumEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodyMediumEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.body_small_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.bodySmallEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(bodySmallEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_large_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelLargeEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelLargeEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_medium_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelMediumEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelMediumEmphasized = value),
            ),
        )
    }

    SettingsTextStyle(
        stringResource(Res.string.label_small_emphasized),
        fontStyles,
        fontSynthesis,
        fontFamilies,
        theme.typography.labelSmallEmphasized,
    ) { value ->
        onThemeChange(
            theme.copy(
                typography = theme.typography.copy(labelSmallEmphasized = value),
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsTypographyScreen(): Unit = SettingsTypographyScreen()

@Suppress("ComposeMultipleContentEmitters")
@Composable
private fun SettingsTextStyle(
    title: String,
    fontStyles: List<FontStyleSerial>,
    fontSynthesis: List<FontSynthesisSerial>,
    fontFamilies: List<FontFamilySerial>,
    value: TextStyle,
    onValueChanged: (TextStyle) -> Unit,
) = SettingsGroup(
    modifier = Modifier,
    enabled = true,
    title = { Text(title) },
    contentPadding = PaddingValues(16.dp),
) {
    SettingsSliderFinished(
        title = stringResource(Res.string.font_size),
        initialValue = value.fontSize.toFloatPx(),
        icon = { Icons.Default.FormatSize },
        enabled = true,
        valueRange = 22f..114f,
        onValueChanged = {
            onValueChanged(value.copy(fontSize = it.sp))
        },
    )

    value.fontWeight?.let { fontWeight ->
        SettingsSliderFinished(
            title = stringResource(Res.string.font_weight),
            initialValue = fontWeight.weight.toFloatPx(),
            icon = { Icons.Default.FormatBold },
            enabled = true,
            valueRange = 1f..1000f,
            steps = 7,
            onValueChanged = {
                onValueChanged(value.copy(fontWeight = FontWeight(it.roundToInt())))
            },
        )
    }

    value.fontStyle?.let { fontStyle ->
        SettingsListPickerDialog(
            fontStyle,
            values = fontStyles,
            title = stringResource(Res.string.font_style),
            icon = Icons.Default.FormatShapes,
            modifier = Modifier,
            enabled = true,
        ) {
            onValueChanged(value.copy(fontStyle = it))
            false
        }
    }

    value.fontSynthesis?.let { _fontSynthesis ->
        SettingsListPickerDialog(
            _fontSynthesis,
            values = fontSynthesis,
            title = stringResource(Res.string.font_synthesis),
            icon = Icons.Default.FormatPaint,
            modifier = Modifier,
            enabled = true,
        ) {
            onValueChanged(value.copy(fontSynthesis = it))
            false
        }
    }

    value.fontFamily?.let { fontFamily ->
        SettingsListPickerDialog(
            fontFamily,
            values = fontFamilies,
            title = stringResource(Res.string.font_family),
            icon = Icons.Default.TextFormat,
            modifier = Modifier,
            enabled = true,
        ) {
            onValueChanged(value.copy(fontFamily = it))
            false
        }
    }

    SettingsSliderFinished(
        title = stringResource(Res.string.line_height),
        initialValue = value.lineHeight.toFloatPx(),
        icon = { Icons.Default.Height },
        enabled = true,
        valueRange = 32f..128f,
        onValueChanged = {
            onValueChanged(value.copy(lineHeight = it.sp))
        },
    )

    SettingsSliderFinished(
        title = stringResource(Res.string.letter_spacing),
        initialValue = value.letterSpacing.toFloatPx(),
        icon = { Icons.Default.LinearScale },
        enabled = true,
        valueRange = -0.4f..1f,
        onValueChanged = {
            onValueChanged(value.copy(letterSpacing = it.sp))
        },
    )
}
