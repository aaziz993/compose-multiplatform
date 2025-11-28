package clib.presentation.theme.shapes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.material3.Shapes as ComposeShapes

@Serializable
private data class Shapes(
    val extraSmall: CornerBasedShapeSerial,
    val small: CornerBasedShapeSerial,
    val medium: CornerBasedShapeSerial,
    val large: CornerBasedShapeSerial,
    val extraLarge: CornerBasedShapeSerial,
)

public object ShapesSerializer : KSerializer<ComposeShapes> {

    override val descriptor: SerialDescriptor = Shapes.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeShapes): Unit =
        encoder.encodeSerializableValue(
            Shapes.serializer(),
            Shapes(
                value.extraSmall,
                value.small,
                value.medium,
                value.large,
                value.extraLarge,
            ),
        )

    override fun deserialize(decoder: Decoder): ComposeShapes =
        decoder.decodeSerializableValue(Shapes.serializer()).let { value ->
            ComposeShapes(
                value.extraSmall,
                value.small,
                value.medium,
                value.large,
                value.extraLarge,
            )
        }
}

public typealias ShapesSerial = @Serializable(with = ShapesSerializer::class) ComposeShapes
