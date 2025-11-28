package clib.presentation.theme.shapes

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Shapes(
    val extraSmall: CornerBasedShape = ShapeDefaults.ExtraSmall,
    val small: CornerBasedShape = ShapeDefaults.Small,
    val medium: CornerBasedShape = ShapeDefaults.Medium,
    val large: CornerBasedShape = ShapeDefaults.Large,
    val extraLarge: CornerBasedShape = ShapeDefaults.ExtraLarge,
) {

    public fun toShapes(): androidx.compose.material3.Shapes =
        androidx.compose.material3.Shapes(
            extraSmall.toCornerBasedShape(),
            small.toCornerBasedShape(),
            medium.toCornerBasedShape(),
            large.toCornerBasedShape(),
            extraLarge.toCornerBasedShape(),
        )
}

public fun androidx.compose.material3.Shapes.toShapes(): Shapes = Shapes(
    extraSmall.toCornerBasedShape(),
    small.toCornerBasedShape(),
    medium.toCornerBasedShape(),
    large.toCornerBasedShape(),
    extraLarge.toCornerBasedShape(),
)
