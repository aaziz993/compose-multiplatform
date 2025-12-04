package clib.presentation.theme.density

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import kotlin.math.roundToInt

// Those functions are designed to be used in lambdas.

// DP
public fun Density.dpToSp(dp: Dp): TextUnit = if (dp.isSpecified) dp.toSp() else TextUnit.Unspecified

public fun Density.dpToFloatPx(dp: Dp): Float = if (dp.isSpecified) dp.toPx() else Float.NaN

public fun Density.dpToIntPx(dp: Dp): Int = if (dp.isSpecified) dp.toPx().toInt() else 0

public fun Density.dpRoundToPx(dp: Dp): Int = if (dp.isSpecified) dp.roundToPx() else 0

@Composable
public fun Dp.toSp(): TextUnit = LocalDensity.current.dpToSp(this)

@Composable
public fun Dp.toFloatPx(): Float = LocalDensity.current.dpToFloatPx(this)

@Composable
public fun Dp.toIntPx(): Int = LocalDensity.current.dpToIntPx(this)

@Composable
public fun Dp.roundToPx(): Int = LocalDensity.current.dpRoundToPx(this)

public fun Dp.toRecDpSize(): DpSize = if (isSpecified) DpSize(this, this) else DpSize.Unspecified

public fun Dp.toRecDpOffset(): DpOffset = if (isSpecified) DpOffset(this, this) else DpOffset.Unspecified

// TEXT UNIT
public fun Density.spToDp(sp: TextUnit): Dp = if (sp.isSpecified) sp.toDp() else Dp.Unspecified

public fun Density.spToFloatPx(sp: TextUnit): Float = if (sp.isSpecified) sp.toPx() else Float.NaN

public fun Density.spToIntPx(sp: TextUnit): Int = if (sp.isSpecified) sp.toPx().toInt() else 0

public fun Density.spRoundToPx(sp: TextUnit): Int = if (sp.isSpecified) sp.roundToPx() else 0

@Composable
public fun TextUnit.toDp(): Dp = LocalDensity.current.spToDp(this)

@Composable
public fun TextUnit.toFloatPx(): Float = LocalDensity.current.spToFloatPx(this)

@Composable
public fun TextUnit.toIntPx(): Int = LocalDensity.current.spToIntPx(this)

@Composable
public fun TextUnit.roundToPx(): Int = LocalDensity.current.spRoundToPx(this)

// FLOAT
public fun Density.floatPxToDp(px: Float): Dp = if (px.isFinite()) px.toDp() else Dp.Unspecified

public fun Density.floatPxToSp(px: Float): TextUnit = if (px.isFinite()) px.toSp() else TextUnit.Unspecified

@Composable
public fun Float.toDp(): Dp = LocalDensity.current.floatPxToDp(this)

@Composable
public fun Float.toSp(): TextUnit = LocalDensity.current.floatPxToSp(this)

public fun Float.toIntPx(): Int = if (isFinite()) toInt() else 0

public fun Float.roundToPx(): Int = if (isFinite()) roundToInt() else 0

public fun Float.toRecSize(): Size = if (isFinite()) Size(this, this) else Size.Unspecified

public fun Float.toRecOffset(): Offset = if (isFinite()) Offset(this, this) else Offset.Unspecified

// INT
public fun Density.intPxToDp(px: Int): Dp = px.toDp()

public fun Density.intPxToSp(px: Int): TextUnit = px.toSp()

@Composable
public fun Int.toDp(): Dp = LocalDensity.current.intPxToDp(this)

@Composable
public fun Int.toSp(): TextUnit = LocalDensity.current.intPxToSp(this)

public fun Int.toFloatPx(): Float = toFloat()

public fun Int.toRecIntSize(): IntSize = IntSize(this, this)

public fun Int.toRecIntOffset(): IntOffset = IntOffset(this, this)

// DP SIZE
public fun Density.dpSizeToIntSize(dpSize: DpSize): IntSize =
    if (dpSize.isSpecified) IntSize(dpSize.width.toPx().toInt(), dpSize.height.toPx().toInt())
    else IntSize.Zero

public fun Density.dpSizeRoundToIntSize(dpSize: DpSize): IntSize =
    if (dpSize.isSpecified) IntSize(dpSize.width.roundToPx(), dpSize.height.roundToPx())
    else IntSize.Zero

public fun Density.dpSizeToSize(dpSize: DpSize): Size =
    if (dpSize.isSpecified) Size(dpSize.width.toPx(), dpSize.height.toPx())
    else Size.Unspecified

@Composable
public fun DpSize.toIntSize(): IntSize = LocalDensity.current.dpSizeToIntSize(this)

@Composable
public fun DpSize.roundToIntSize(): IntSize = LocalDensity.current.dpSizeRoundToIntSize(this)

@Composable
public fun DpSize.toSize(): Size = LocalDensity.current.dpSizeToSize(this)

public fun DpSize.isSpaced(): Boolean = isSpecified && width > 0.dp && height > 0.dp

// SIZE
public fun Density.sizeToDpSize(size: Size): DpSize =
    if (size.isSpecified) DpSize(size.width.toDp(), size.height.toDp())
    else DpSize.Unspecified

@Composable
public fun Size.toDpSize(): DpSize =
    if (isSpecified) LocalDensity.current.sizeToDpSize(this)
    else DpSize.Unspecified

public fun Size.toIntSize(): IntSize =
    if (isSpecified) IntSize(width.toInt(), height.toInt())
    else IntSize.Zero

public fun Size.isSpaced(): Boolean = isSpecified && width > 0F && height > 0F

// INT SIZE
public fun Density.intSizeToDpSize(intSize: IntSize): DpSize =
    DpSize(intSize.width.toDp(), intSize.height.toDp())

@Composable
public fun IntSize.toDpSize(): DpSize = LocalDensity.current.intSizeToDpSize(this)

@Composable
public fun IntSize.toSize(): Size = Size(width.toFloat(), height.toFloat())

public fun IntSize.isSpaced(): Boolean = width > 0 && height > 0

// DP OFFSET
public fun Density.dpOffsetToIntOffset(dpOffset: DpOffset): IntOffset =
    if (dpOffset.isSpecified) IntOffset(dpOffset.x.toPx().toInt(), dpOffset.y.toPx().toInt())
    else IntOffset.Zero

public fun Density.dpOffsetRoundToIntOffset(dpOffset: DpOffset): IntOffset =
    if (dpOffset.isSpecified) IntOffset(dpOffset.x.roundToPx(), dpOffset.y.roundToPx())
    else IntOffset.Zero

public fun Density.dpOffsetToOffset(dpOffset: DpOffset): Offset =
    if (dpOffset.isSpecified) Offset(dpOffset.x.toPx(), dpOffset.y.toPx())
    else Offset.Unspecified

@Composable
public fun DpOffset.toIntOffset(): IntOffset = LocalDensity.current.dpOffsetToIntOffset(this)

@Composable
public fun DpOffset.roundToIntOffset(): IntOffset = LocalDensity.current.dpOffsetRoundToIntOffset(this)

@Composable
public fun DpOffset.toOffset(): Offset = LocalDensity.current.dpOffsetToOffset(this)

// OFFSET
public fun Density.offsetToDpOffset(offset: Offset): DpOffset =
    if (offset.isSpecified) DpOffset(offset.x.toDp(), offset.y.toDp())
    else DpOffset.Unspecified

@Composable
public fun Offset.toDpOffset(): DpOffset = LocalDensity.current.offsetToDpOffset(this)

public fun Offset.toIntOffset(): IntOffset =
    if (isSpecified) IntOffset(x.toInt(), y.toInt())
    else IntOffset.Zero

// INT OFFSET
public fun Density.intOffsetToDpOffset(intOffset: IntOffset): DpOffset =
    DpOffset(intOffset.x.toDp(), intOffset.y.toDp())

@Composable
public fun IntOffset.toDpOffset(): DpOffset = LocalDensity.current.intOffsetToDpOffset(this)

public fun IntOffset.toOffset(): Offset = Offset(x.toFloat(), y.toFloat())
