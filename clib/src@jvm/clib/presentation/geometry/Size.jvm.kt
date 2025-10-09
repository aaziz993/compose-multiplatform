package clib.presentation.geometry

import androidx.compose.ui.geometry.Size
import java.awt.Dimension

public fun Size.toDimension(): Dimension = Dimension(width.toInt(), height.toInt())

