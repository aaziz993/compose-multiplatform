package clib.presentation.theme.model

import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import androidx.compose.animation.core.CubicBezierEasing as ComposeCubicBezierEasing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.animation.core.Easing as ComposeEasing

@Serializable(EasingSerializer::class)
private sealed class Easing {

    abstract fun toEasing(): ComposeEasing
}

private object EasingSerializer : MapTransformingPolymorphicSerializer<Easing>(
    baseClass = Easing::class,
    subclasses = mapOf(
        CubicBezierEasing::class to CubicBezierEasing.serializer(),
    ),
)

@Serializable
@SerialName("cubicBezier")
private data class CubicBezierEasing(
    val a: Float,
    val b: Float,
    val c: Float,
    val d: Float,
) : Easing() {

    override fun toEasing(): ComposeEasing = ComposeCubicBezierEasing(a, b, c, d)
}

public object ComposeEasingSerializer : KSerializer<ComposeEasing> {

    override val descriptor: SerialDescriptor = Easing.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeEasing): Unit =
        encoder.encodeSerializableValue(
            Easing.serializer(),
            when (value) {
                is ComposeCubicBezierEasing ->
                    value.toString()
                        .removePrefix("CubicBezierEasing(")
                        .removeSuffix(")")
                        .split(",")
                        .map { value -> value.substringAfter("=").toFloat() }
                        .let { (a, b, c, d) ->
                            CubicBezierEasing(a, b, c, d)
                        }

                else -> throw IllegalArgumentException("Unknown easing '$value'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeEasing =
        decoder.decodeSerializableValue(Easing.serializer()).toEasing()
}

public typealias EasingSerial = @Serializable(with = ComposeEasingSerializer::class) ComposeEasing
