package ui.settings

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.components.model.item.Item
import clib.presentation.theme.model.Theme
import com.materialkolor.scheme.DynamicScheme
import compose_app.generated.resources.Res
import compose_app.generated.resources.amoled
import compose_app.generated.resources.animate
import compose_app.generated.resources.contrast
import compose_app.generated.resources.damping_ratio
import compose_app.generated.resources.delay
import compose_app.generated.resources.duration
import compose_app.generated.resources.easing
import compose_app.generated.resources.error
import compose_app.generated.resources.neutral
import compose_app.generated.resources.neutral_variant
import compose_app.generated.resources.platform
import compose_app.generated.resources.primary
import compose_app.generated.resources.secondary
import compose_app.generated.resources.seed_color
import compose_app.generated.resources.stiffness
import compose_app.generated.resources.tertiary
import compose_app.generated.resources.visibility_threshold
import data.type.primitives.string.asStringResource
import klib.data.type.cast
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsColorPickerBottomSheet
import presentation.components.settings.SettingsListPickerDialog
import presentation.components.settings.SettingsSlider
import presentation.components.settings.SettingsSwitch
import ui.navigation.presentation.SettingsDynamicColorScheme

@Composable
public fun SettingsDynamicColorSchemeScreen(
    modifier: Modifier = Modifier,
    route: SettingsDynamicColorScheme = SettingsDynamicColorScheme,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val colorScheme = theme.currentDynamicColorScheme

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.seed_color),
        colorScheme.seedColor,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(seedColor = value)))
    }

    SettingsSwitch(
        title = stringResource(Res.string.amoled),
        value = colorScheme.isAmoled,
        trueIcon = Icons.Outlined.SmartDisplay,
        falseIcon = Icons.Filled.SmartDisplay,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(isAmoled = value)))
        },
    )

    colorScheme.primary?.let { primary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.primary),
            primary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(primary = value)))
        }
    }

    colorScheme.secondary?.let { secondary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.secondary),
            secondary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(secondary = value)))
        }
    }

    colorScheme.tertiary?.let { tertiary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.tertiary),
            tertiary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(tertiary = value)))
        }
    }

    colorScheme.neutral?.let { neutral ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral),
            neutral,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(neutral = value)))
        }
    }

    colorScheme.neutralVariant?.let { neutralVariant ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral_variant),
            neutralVariant,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(neutralVariant = value)))
        }
    }

    colorScheme.error?.let { error ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.error),
            error,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(error = value)))
        }
    }

    SettingsSlider(
        title = stringResource(Res.string.contrast),
        value = colorScheme.contrastLevel.toFloat(),
        icon = Icons.Default.Contrast,
        enabled = true,
        valueRange = -1f..1f,
    ) { value, _ ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(contrastLevel = value.toDouble())))
    }

    val platforms = remember { DynamicScheme.Platform.entries.toList() }

    SettingsListPickerDialog(
        colorScheme.platform,
        values = platforms,
        title = stringResource(Res.string.platform),
        icon = Icons.Default.Devices,
        modifier = Modifier,
        enabled = true,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(platform = value)))
        false
    }

    SettingsSwitch(
        title = stringResource(Res.string.animate),
        value = colorScheme.animate,
        trueIcon = Icons.Outlined.Animation,
        falseIcon = Icons.Filled.Animation,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(animate = value)))
        },
    )

    colorScheme.animationSpec?.let { animationSpec ->
        val animationSpecs = remember {
            listOf<AnimationSpec<Color>>(
                spring(),
                tween(),
            )
        }

        SettingsListPickerDialog(
            animationSpec,
            values = animationSpecs,
            title = stringResource(Res.string.animate),
            icon = Icons.Default.Animation,
            modifier = Modifier,
            enabled = true,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(animationSpec = value)))
            false
        }

        when (val animationSpec = colorScheme.animationSpec) {
            is SpringSpec<*> -> SettingsGroupSpringSpec(
                animationSpec.cast(),
            ) { value ->
                onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(animationSpec = value)))
            }

            is TweenSpec<*> -> SettingsGroupTweenSpec(
                animationSpec.cast(),
            ) { value ->
                onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(animationSpec = value)))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsDynamicColorSchemeScreen(): Unit = SettingsDynamicColorSchemeScreen()

@Composable
private fun SettingsGroupSpringSpec(
    value: SpringSpec<Color>,
    onValueChange: (SpringSpec<Color>) -> Unit,
) {
    SettingsSlider(
        title = stringResource(Res.string.damping_ratio),
        value = value.dampingRatio,
        icon = Icons.Default.AspectRatio,
        enabled = true,
        valueRange = 0.01f..1f,
    ) { it, _ ->
        onValueChange(
            spring(
                it,
                value.stiffness,
                value.visibilityThreshold,
            ),
        )
    }

    SettingsSlider(
        title = stringResource(Res.string.stiffness),
        value = value.stiffness,
        icon = Icons.Default.Scale,
        enabled = true,
        valueRange = 200f..10_000f,
    ) { it, _ ->
        onValueChange(
            spring(
                value.dampingRatio,
                it,
                value.visibilityThreshold,
            ),
        )
    }

    value.visibilityThreshold?.let { visibilityThreshold ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.visibility_threshold),
            visibilityThreshold,
        ) {
            onValueChange(
                spring(
                    value.dampingRatio,
                    value.stiffness,
                    it,
                ),
            )
        }
    }
}

@Composable
private fun SettingsGroupTweenSpec(
    value: TweenSpec<Color>,
    onValueChange: (TweenSpec<Color>) -> Unit,
) {
    SettingsSlider(
        title = stringResource(Res.string.duration),
        value = value.durationMillis.toFloat(),
        icon = Icons.Default.Timelapse,
        enabled = true,
        valueRange = 100f..5_000f,
    ) { it, _ ->
        onValueChange(
            tween(
                it.toInt(),
                value.delay,
                value.easing,
            ),
        )
    }

    SettingsSlider(
        title = stringResource(Res.string.delay),
        value = value.delay.toFloat(),
        icon = Icons.Default.Timer,
        enabled = true,
        valueRange = 100f..5_000f,
    ) { it, _ ->
        onValueChange(
            tween(
                value.durationMillis,
                it.toInt(),
                value.easing,
            ),
        )
    }

    val easings = remember {
        listOf(
            FastOutSlowInEasing,
            LinearOutSlowInEasing,
            FastOutLinearInEasing,
            LinearEasing,
        )
    }

    SettingsListPickerDialog(
        value.easing,
        values = easings,
        title = stringResource(Res.string.easing),
        icon = Icons.Default.Functions,
        modifier = Modifier,
        enabled = true,
    ) {
        onValueChange(
            tween(
                value.durationMillis,
                value.delay,
                it,
            ),
        )
        false
    }
}

