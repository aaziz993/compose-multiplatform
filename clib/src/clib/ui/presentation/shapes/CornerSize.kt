package clib.ui.presentation.shapes

import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.ui.unit.Dp
import kotlinx.serialization.Serializable

@Serializable
public sealed class CornerSize {

    public abstract fun toCornerSize(): androidx.compose.foundation.shape.CornerSize
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

    override fun toCornerSize(): androidx.compose.foundation.shape.CornerSize = ZeroCornerSize
}

