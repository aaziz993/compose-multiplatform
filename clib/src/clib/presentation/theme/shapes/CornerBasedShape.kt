package clib.presentation.theme.shapes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
public sealed class CornerBasedShape {

    public abstract val topStart: CornerSize
    public abstract val topEnd: CornerSize
    public abstract val bottomEnd: CornerSize
    public abstract val bottomStart: CornerSize

    public abstract fun toCornerBasedShape(): androidx.compose.foundation.shape.CornerBasedShape
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

public val CircleShape: RoundedCornerShape = RoundedCornerShape(50)

public fun RoundedCornerShape(corner: CornerSize): RoundedCornerShape =
    RoundedCornerShape(corner, corner, corner, corner)

public fun RoundedCornerShape(size: Dp): RoundedCornerShape = RoundedCornerShape(CornerSize(size))

public fun RoundedCornerShape(size: Float): RoundedCornerShape = RoundedCornerShape(CornerSize(size))

public fun RoundedCornerShape(percent: Int): RoundedCornerShape = RoundedCornerShape(CornerSize(percent))

public fun RoundedCornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
): RoundedCornerShape = RoundedCornerShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart),
)

public fun RoundedCornerShape(
    topStart: Float = 0.0f,
    topEnd: Float = 0.0f,
    bottomEnd: Float = 0.0f,
    bottomStart: Float = 0.0f,
): RoundedCornerShape = RoundedCornerShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart),
)

public fun RoundedCornerShape(
    topStartPercent: Int = 0,
    topEndPercent: Int = 0,
    bottomEndPercent: Int = 0,
    bottomStartPercent: Int = 0,
): RoundedCornerShape = RoundedCornerShape(
    topStart = CornerSize(topStartPercent),
    topEnd = CornerSize(topEndPercent),
    bottomEnd = CornerSize(bottomEndPercent),
    bottomStart = CornerSize(bottomStartPercent),
)


