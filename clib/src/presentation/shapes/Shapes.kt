package presentation.shapes

import androidx.compose.material3.Shapes
import kotlinx.serialization.Serializable

@Serializable
public data class Shapes(
    val extraSmall: CornerBasedShape = ShapeDefaults.ExtraSmall,
    val small: CornerBasedShape = ShapeDefaults.Small,
    val medium: CornerBasedShape = ShapeDefaults.Medium,
    val large: CornerBasedShape = ShapeDefaults.Large,
    val extraLarge: CornerBasedShape = ShapeDefaults.ExtraLarge,
) {

    public fun toShapes(): Shapes =
        Shapes(
            extraSmall.toCornerBasedShape(),
            small.toCornerBasedShape(),
            medium.toCornerBasedShape(),
            large.toCornerBasedShape(),
            extraLarge.toCornerBasedShape(),
        )
}
