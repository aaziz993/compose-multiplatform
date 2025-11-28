package presentation.theme

import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import clib.presentation.theme.shapes.squircleshape.CornerSmoothing
import clib.presentation.theme.shapes.squircleshape.SquircleShape

public val SquircleShapes: Shapes = Shapes(
    small = SquircleShape(radius = 16.dp, smoothing = CornerSmoothing.Medium),
    medium = SquircleShape(radius = 32.dp, smoothing = CornerSmoothing.Medium),
    large = SquircleShape(percent = 100, smoothing = CornerSmoothing.Medium),
)
