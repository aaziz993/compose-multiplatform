package clib.presentation.theme.shapes

import androidx.compose.runtime.Immutable
import clib.presentation.theme.shapes.squircleshape.CornerSmoothing
import klib.data.type.cast
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed class CornerBasedShape {

    public abstract val topStart: CornerSize
    public abstract val topEnd: CornerSize
    public abstract val bottomEnd: CornerSize
    public abstract val bottomStart: CornerSize

    public abstract fun toCornerBasedShape(): androidx.compose.foundation.shape.CornerBasedShape
}

public fun androidx.compose.foundation.shape.CornerBasedShape.toCornerBasedShape(): CornerBasedShape =
    when (this) {
        is androidx.compose.foundation.shape.RoundedCornerShape -> RoundedCornerShape(
            topStart.toCornerSize(),
            topEnd.toCornerSize(),
            bottomEnd.toCornerSize(),
            bottomStart.toCornerSize(),
        )

        is clib.presentation.theme.shapes.squircleshape.SquircleShape -> SquircleShape(
            topStart.toCornerSize(),
            topEnd.toCornerSize(),
            bottomEnd.toCornerSize(),
            bottomStart.toCornerSize(),
            smoothing,
        )

        else -> throw IllegalArgumentException("Unknown corner shape '$this'")
    }

@Serializable
public data class RoundedCornerShape(
    override val topStart: CornerSize,
    override val topEnd: CornerSize,
    override val bottomEnd: CornerSize,
    override val bottomStart: CornerSize,
) : CornerBasedShape() {

    override fun toCornerBasedShape(): androidx.compose.foundation.shape.CornerBasedShape =
        androidx.compose.foundation.shape.RoundedCornerShape(
            topStart.toCornerSize(),
            topEnd.toCornerSize(),
            bottomEnd.toCornerSize(),
            bottomStart.toCornerSize(),
        )
}

public val CircleShape: RoundedCornerShape =
    androidx.compose.foundation.shape.CircleShape.toCornerBasedShape().cast()

@Serializable
public sealed class SquircleBasedShape : CornerBasedShape() {

    public abstract val smoothing: Int
}

@Serializable
public data class SquircleShape(
    override val topStart: CornerSize = CornerSize(100),
    override val topEnd: CornerSize = CornerSize(100),
    override val bottomStart: CornerSize = CornerSize(100),
    override val bottomEnd: CornerSize = CornerSize(100),
    override val smoothing: Int = CornerSmoothing.Medium
) : SquircleBasedShape() {

    override fun toCornerBasedShape(): androidx.compose.foundation.shape.CornerBasedShape =
        clib.presentation.theme.shapes.squircleshape.SquircleShape(
            topStart.toCornerSize(),
            topEnd.toCornerSize(),
            bottomEnd.toCornerSize(),
            bottomStart.toCornerSize(),
            smoothing,
        )
}
