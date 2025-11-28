package clib.presentation.theme.density.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.ui.unit.Density as ComposeDensity

@Serializable
private data class Density(
    val density: Float,
    val fontScale: Float,
)

public object DensitySerializer : KSerializer<ComposeDensity> {

    override val descriptor: SerialDescriptor = Density.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeDensity): Unit =
        encoder.encodeSerializableValue(
            Density.serializer(),
            Density(value.density, value.fontScale),
        )

    override fun deserialize(decoder: Decoder): ComposeDensity =
        decoder.decodeSerializableValue(Density.serializer()).let { value ->
            ComposeDensity(value.density, value.fontScale)
        }
}
public typealias DensitySerial = @Serializable(with = DensitySerializer::class) ComposeDensity
