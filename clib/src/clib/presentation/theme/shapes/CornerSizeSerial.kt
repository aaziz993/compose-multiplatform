package clib.presentation.theme.shapes

import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.unit.Dp
import clib.data.type.DpSerial
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.foundation.shape.CornerSize as ComposeCornerSize
import androidx.compose.foundation.shape.ZeroCornerSize as ComposeZeroCornerSize

@Serializable
private sealed class CornerSize {

    abstract fun toCornerSize(): ComposeCornerSize
}

@Serializable
private data class DpCornerSize(private val size: DpSerial) : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize =
        ComposeCornerSize(size)
}

@Serializable
private data class PxCornerSize(private val size: Float) : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize =
        ComposeCornerSize(size)
}

@Serializable
private data class PercentCornerSize(private val percent: Float) : CornerSize() {

    init {
        if (percent < 0 || percent > 100) {
            throw IllegalArgumentException("The percent should be in the range of [0, 100]")
        }
    }

    override fun toCornerSize(): ComposeCornerSize = ComposeCornerSize(percent)
}

private object ZeroCornerSize : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize = ComposeZeroCornerSize
}

public object CornerSizeSerializer : KSerializer<ComposeCornerSize> {

    override val descriptor: SerialDescriptor = CornerSize.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeCornerSize): Unit =
        encoder.encodeSerializableValue(
            CornerSize.serializer(),
            when (val valueOverride = (value as InspectableValue).valueOverride) {
                is Dp -> DpCornerSize(valueOverride)
                is String -> when {
                    valueOverride == "ZeroCornerSize" -> ZeroCornerSize
                    valueOverride.endsWith("px") -> PxCornerSize(valueOverride.removeSuffix("px").toFloat())
                    valueOverride.endsWith("%") ->
                        PercentCornerSize(valueOverride.removeSuffix("%").toFloat())

                    else -> throw IllegalArgumentException("Unknown corner size '$this'")
                }

                else -> throw IllegalArgumentException("Unknown corner size '$this'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeCornerSize =
        decoder.decodeSerializableValue(CornerSize.serializer()).toCornerSize()
}

public typealias CornerSizeSerial = @Serializable(with = CornerSizeSerializer::class) ComposeCornerSize
