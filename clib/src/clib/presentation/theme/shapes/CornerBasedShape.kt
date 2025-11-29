package clib.presentation.theme.shapes

import clib.presentation.theme.shapes.squircleshape.CornerSmoothing
import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.foundation.shape.CornerBasedShape as ComposeCornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape as ComposeRoundedCornerShape
import clib.presentation.theme.shapes.squircleshape.SquircleShape as ComposeSquircleShape

@Serializable(CornerBasedShapeSerializer::class)
private sealed class CornerBasedShape {

    abstract val topStart: CornerSizeSerial
    abstract val topEnd: CornerSizeSerial
    abstract val bottomEnd: CornerSizeSerial
    abstract val bottomStart: CornerSizeSerial

    abstract fun toCornerBasedShape(): ComposeCornerBasedShape
}

private object CornerBasedShapeSerializer : MapTransformingPolymorphicSerializer<CornerBasedShape>(
    baseClass = CornerBasedShape::class,
    subclasses = mapOf(
        RoundedCornerShape::class to RoundedCornerShape.serializer(),
        SquircleShape::class to SquircleShape.serializer(),
    ),
)

@Serializable
@SerialName("rounded")
private data class RoundedCornerShape(
    override val topStart: CornerSizeSerial,
    override val topEnd: CornerSizeSerial,
    override val bottomEnd: CornerSizeSerial,
    override val bottomStart: CornerSizeSerial,
) : CornerBasedShape() {

    override fun toCornerBasedShape(): ComposeCornerBasedShape =
        ComposeRoundedCornerShape(
            topStart,
            topEnd,
            bottomEnd,
            bottomStart,
        )
}

private sealed class SquircleBasedShape : CornerBasedShape() {

    abstract val smoothing: Int
}

@Serializable
@SerialName("squircle")
private data class SquircleShape(
    override val topStart: CornerSizeSerial,
    override val topEnd: CornerSizeSerial,
    override val bottomStart: CornerSizeSerial,
    override val bottomEnd: CornerSizeSerial,
    override val smoothing: Int = CornerSmoothing.Medium
) : SquircleBasedShape() {

    override fun toCornerBasedShape(): ComposeCornerBasedShape =
        clib.presentation.theme.shapes.squircleshape.SquircleShape(
            topStart,
            topEnd,
            bottomEnd,
            bottomStart,
            smoothing,
        )
}

public object ComposeCornerBasedShapeSerializer : KSerializer<ComposeCornerBasedShape> {

    override val descriptor: SerialDescriptor = CornerBasedShape.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeCornerBasedShape): Unit =
        encoder.encodeSerializableValue(
            CornerBasedShape.serializer(),
            when (value) {
                is ComposeRoundedCornerShape -> RoundedCornerShape(
                    value.topStart,
                    value.topEnd,
                    value.bottomEnd,
                    value.bottomStart,
                )

                is ComposeSquircleShape -> SquircleShape(
                    value.topStart,
                    value.topEnd,
                    value.bottomEnd,
                    value.bottomStart,
                    value.smoothing,
                )

                else -> throw IllegalArgumentException("Unknown corner shape '$value'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeCornerBasedShape =
        decoder.decodeSerializableValue(CornerBasedShape.serializer()).toCornerBasedShape()
}

public typealias CornerBasedShapeSerial = @Serializable(with = ComposeCornerBasedShapeSerializer::class) ComposeCornerBasedShape
