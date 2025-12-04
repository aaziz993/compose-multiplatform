package clib.presentation.theme.shapes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.material3.Shapes as ComposeShapes

private val SHAPES = ComposeShapes()

@Serializable
private data class Shapes(
    val extraSmall: CornerBasedShapeSerial = SHAPES.extraSmall,
    val small: CornerBasedShapeSerial = SHAPES.small,
    val medium: CornerBasedShapeSerial = SHAPES.medium,
    val large: CornerBasedShapeSerial = SHAPES.large,
    val extraLarge: CornerBasedShapeSerial = SHAPES.extraLarge,
)

public object ShapesSerializer : KSerializer<ComposeShapes> {

    private val delegate = Shapes.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ComposeShapes): Unit =
        encoder.encodeSerializableValue(
            delegate,
            Shapes(
                value.extraSmall,
                value.small,
                value.medium,
                value.large,
                value.extraLarge,
            ),
        )

    override fun deserialize(decoder: Decoder): ComposeShapes =
        decoder.decodeSerializableValue(delegate).let { value ->
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
