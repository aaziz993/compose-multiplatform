package clib.presentation.theme.model

import androidx.compose.material3.ColorScheme
import clib.data.type.primitives.color.adjustContrast
import clib.presentation.theme.Error
import clib.presentation.theme.ErrorContainer
import clib.presentation.theme.InversePrimary
import clib.presentation.theme.OnError
import clib.presentation.theme.OnErrorContainer
import clib.presentation.theme.OnPrimaryContainer
import clib.presentation.theme.OnPrimaryFixed
import clib.presentation.theme.OnPrimaryFixedVariant
import clib.presentation.theme.OnSecondary
import clib.presentation.theme.OnSecondaryContainer
import clib.presentation.theme.OnSecondaryFixed
import clib.presentation.theme.OnSecondaryFixedVariant
import clib.presentation.theme.OnTertiary
import clib.presentation.theme.OnTertiaryContainer
import clib.presentation.theme.OnTertiaryFixed
import clib.presentation.theme.OnTertiaryFixedVariant
import clib.presentation.theme.PrimaryContainer
import clib.presentation.theme.PrimaryFixed
import clib.presentation.theme.PrimaryFixedDim
import clib.presentation.theme.Secondary
import clib.presentation.theme.SecondaryContainer
import clib.presentation.theme.SecondaryFixed
import clib.presentation.theme.SecondaryFixedDim
import clib.presentation.theme.Tertiary
import clib.presentation.theme.TertiaryContainer
import clib.presentation.theme.TertiaryFixed
import clib.presentation.theme.TertiaryFixedDim
import com.materialkolor.scheme.DynamicScheme.Platform

public fun ColorScheme.adjustContrast(
    contrastLevel: Double,
    platform: Platform
): ColorScheme = copy(
    primaryContainer = PrimaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        primaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: primaryContainer,
    onPrimaryContainer = OnPrimaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        onPrimaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: onPrimaryContainer,
    inversePrimary = InversePrimary.getCurve(contrastLevel, platform)?.let { curve ->
        inversePrimary.adjustContrast(curve.get(contrastLevel))
    } ?: inversePrimary,
    secondary = Secondary.getCurve(contrastLevel, platform)?.let { curve ->
        secondary.adjustContrast(curve.get(contrastLevel))
    } ?: secondary,
    onSecondary = OnSecondary.getCurve(contrastLevel, platform)?.let { curve ->
        onSecondary.adjustContrast(curve.get(contrastLevel))
    } ?: onSecondary,
    secondaryContainer = SecondaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        secondaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: secondaryContainer,
    onSecondaryContainer = OnSecondaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        onSecondaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: onSecondaryContainer,
    tertiary = Tertiary.getCurve(contrastLevel, platform)?.let { curve ->
        tertiary.adjustContrast(curve.get(contrastLevel))
    } ?: tertiary,
    onTertiary = OnTertiary.getCurve(contrastLevel, platform)?.let { curve ->
        onTertiary.adjustContrast(curve.get(contrastLevel))
    } ?: onTertiary,
    tertiaryContainer = TertiaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        tertiaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: tertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer.getCurve(contrastLevel, platform)?.let { curve ->
        onTertiaryContainer.adjustContrast(curve.get(contrastLevel))
    } ?: onTertiaryContainer,
    error = Error.getCurve(contrastLevel, platform)?.let { curve ->
        error.adjustContrast(curve.get(contrastLevel))
    } ?: error,
    onError = OnError.getCurve(contrastLevel, platform)?.let { curve ->
        onError.adjustContrast(curve.get(contrastLevel))
    } ?: onError,
    errorContainer = ErrorContainer.getCurve(contrastLevel, platform)?.let { curve ->
        errorContainer.adjustContrast(curve.get(contrastLevel))
    } ?: errorContainer,
    onErrorContainer = OnErrorContainer.getCurve(contrastLevel, platform)?.let { curve ->
        onErrorContainer.adjustContrast(curve.get(contrastLevel))
    } ?: onErrorContainer,
    primaryFixed = PrimaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        primaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: primaryFixed,
    primaryFixedDim = PrimaryFixedDim.getCurve(contrastLevel, platform)?.let { curve ->
        primaryFixedDim.adjustContrast(curve.get(contrastLevel))
    } ?: primaryFixedDim,
    onPrimaryFixed = OnPrimaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        onPrimaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: onPrimaryFixed,
    onPrimaryFixedVariant = OnPrimaryFixedVariant.getCurve(contrastLevel, platform)?.let { curve ->
        onPrimaryFixedVariant.adjustContrast(curve.get(contrastLevel))
    } ?: onPrimaryFixedVariant,
    secondaryFixed = SecondaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        secondaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: secondaryFixed,
    secondaryFixedDim = SecondaryFixedDim.getCurve(contrastLevel, platform)?.let { curve ->
        secondaryFixedDim.adjustContrast(curve.get(contrastLevel))
    } ?: secondaryFixedDim,
    onSecondaryFixed = OnSecondaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        onSecondaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: onSecondaryFixed,
    onSecondaryFixedVariant = OnSecondaryFixedVariant.getCurve(contrastLevel, platform)?.let { curve ->
        onSecondaryFixedVariant.adjustContrast(curve.get(contrastLevel))
    } ?: onSecondaryFixedVariant,
    tertiaryFixed = TertiaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        tertiaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: tertiaryFixed,
    tertiaryFixedDim = TertiaryFixedDim.getCurve(contrastLevel, platform)?.let { curve ->
        tertiaryFixedDim.adjustContrast(curve.get(contrastLevel))
    } ?: tertiaryFixedDim,
    onTertiaryFixed = OnTertiaryFixed.getCurve(contrastLevel, platform)?.let { curve ->
        onTertiaryFixed.adjustContrast(curve.get(contrastLevel))
    } ?: onTertiaryFixed,
    onTertiaryFixedVariant = OnTertiaryFixedVariant.getCurve(contrastLevel, platform)?.let { curve ->
        onTertiaryFixedVariant.adjustContrast(curve.get(contrastLevel))
    } ?: onTertiaryFixedVariant,
)
