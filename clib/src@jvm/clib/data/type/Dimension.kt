package clib.data.type

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import java.awt.Dimension

public fun IntSize.toDimension(): Dimension = Dimension(width, height)

public fun IntOffset.toDimension(): Dimension = Dimension(x, y)
