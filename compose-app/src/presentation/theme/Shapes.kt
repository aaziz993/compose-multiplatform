package presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import clib.presentation.shapes.squircleshape.CornerSmoothing
import clib.presentation.shapes.squircleshape.SquircleShape

public val Shapes: Shapes = Shapes(
    small = RoundedCornerShape(SmallSpacing),
    medium = RoundedCornerShape(MediumSpacing),
    large = RoundedCornerShape(LargeSpacing),
)

public val SquircleShapes: Shapes = Shapes(
    small = SquircleShape(radius = 16.dp, smoothing = CornerSmoothing.Medium),
    medium = SquircleShape(radius = 32.dp, smoothing = CornerSmoothing.Medium),
    large = SquircleShape(percent = 100, smoothing = CornerSmoothing.Medium),
)
