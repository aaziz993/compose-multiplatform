package clib.presentation.theme.shapes

import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.unit.Dp
import clib.data.type.DpSerial
import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.foundation.shape.CornerSize as ComposeCornerSize
import androidx.compose.foundation.shape.ZeroCornerSize as ComposeZeroCornerSize

@Serializable(CornerSizeSerializer::class)
private sealed class CornerSize {

    abstract fun toCornerSize(): ComposeCornerSize
}

private object CornerSizeSerializer : MapTransformingPolymorphicSerializer<CornerSize>(
    baseClass = CornerSize::class,
    subclasses = mapOf(
        DpCornerSize::class to DpCornerSize.serializer(),
        PxCornerSize::class to PxCornerSize.serializer(),
        PercentCornerSize::class to PercentCornerSize.serializer(),
    ),
)

@Serializable
@SerialName("dp")
private data class DpCornerSize(private val size: DpSerial) : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize =
        ComposeCornerSize(size)
}

@Serializable
@SerialName("px")
private data class PxCornerSize(private val size: Float) : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize =
        ComposeCornerSize(size)
}

@Serializable
@SerialName("percent")
private data class PercentCornerSize(private val percent: Float) : CornerSize() {

    init {
        if (percent < 0 || percent > 100) {
            throw IllegalArgumentException("The percent should be in the range of [0, 100]")
        }
    }

    override fun toCornerSize(): ComposeCornerSize = ComposeCornerSize(percent)
}

@Serializable
@SerialName("zero")
private object ZeroCornerSize : CornerSize() {

    override fun toCornerSize(): ComposeCornerSize = ComposeZeroCornerSize
}

public object ComposeCornerSizeSerializer : KSerializer<ComposeCornerSize> {

    override val descriptor: SerialDescriptor = CornerSize.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeCornerSize): Unit =
        encoder.encodeSerializableValue(
            CornerSize.serializer(),
            when (val valueOverride = (value as InspectableValue).valueOverride) {
                is Dp -> DpCornerSize(valueOverride)
                is String -> when {
                    valueOverride.endsWith("px") -> PxCornerSize(valueOverride.removeSuffix("px").toFloat())
                    valueOverride.endsWith("%") ->
                        PercentCornerSize(valueOverride.removeSuffix("%").toFloat())

                    valueOverride == "ZeroCornerSize" -> ZeroCornerSize

                    else -> throw IllegalArgumentException("Unknown corner size '$value'")
                }

                else -> throw IllegalArgumentException("Unknown corner size '$value'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeCornerSize =
        decoder.decodeSerializableValue(CornerSize.serializer()).toCornerSize()
}

public typealias CornerSizeSerial = @Serializable(with = ComposeCornerSizeSerializer::class) ComposeCornerSize
