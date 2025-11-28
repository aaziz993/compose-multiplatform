package clib.presentation.theme.shapes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.unit.Dp
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed class CornerSize {

    public abstract fun toCornerSize(): androidx.compose.foundation.shape.CornerSize
}

public fun androidx.compose.foundation.shape.CornerSize.toCornerSize(): CornerSize =
    when (val valueOverride = (this as InspectableValue).valueOverride) {
        is Dp -> CornerSize(valueOverride)
        is String -> when {
            valueOverride.endsWith("px") -> CornerSize(valueOverride.removeSuffix("px").toFloat())
            valueOverride.endsWith("%") ->
                CornerSize(valueOverride.removeSuffix("%").toFloat().toInt())

            valueOverride == "ZeroCornerSize" -> ZeroCornerSize

            else -> throw IllegalArgumentException("Unknown corner size '$this'")
        }

        else -> throw IllegalArgumentException("Unknown corner size '$this'")
    }

@Serializable
public data class DpCornerSize(private val size: DpSerial) : CornerSize() {

    override fun toCornerSize(): androidx.compose.foundation.shape.CornerSize =
        androidx.compose.foundation.shape.CornerSize(size)
}

public fun CornerSize(size: Dp): CornerSize = DpCornerSize(size)

@Serializable
public data class PxCornerSize(private val size: Float) : CornerSize() {

    override fun toCornerSize(): androidx.compose.foundation.shape.CornerSize =
        androidx.compose.foundation.shape.CornerSize(size)
}

public fun CornerSize(size: Float): CornerSize = PxCornerSize(size)

@Serializable
public data class PercentCornerSize(private val percent: Float) : CornerSize() {

    init {
        if (percent < 0 || percent > 100) {
            throw IllegalArgumentException("The percent should be in the range of [0, 100]")
        }
    }

    override fun toCornerSize(): androidx.compose.foundation.shape.CornerSize =
        androidx.compose.foundation.shape.CornerSize(percent)
}

public fun CornerSize(percent: Int): CornerSize = PercentCornerSize(percent.toFloat())

public object ZeroCornerSize : CornerSize() {

    override fun toCornerSize(): androidx.compose.foundation.shape.CornerSize =
        androidx.compose.foundation.shape.ZeroCornerSize
}

