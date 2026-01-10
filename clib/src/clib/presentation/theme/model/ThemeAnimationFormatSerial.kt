package clib.presentation.theme.model

import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import io.github.themeanimator.ThemeAnimationFormat as ComposeThemeAnimationFormat

@Serializable(ThemeAnimationFormatSerializer::class)
private sealed class ThemeAnimationFormat {

    abstract fun toThemeAnimationFormat(): ComposeThemeAnimationFormat
}

@Serializable
@SerialName("crossfade")
private object Crossfade : ThemeAnimationFormat() {

    override fun toThemeAnimationFormat(): ComposeThemeAnimationFormat = ComposeThemeAnimationFormat.Crossfade
}

@Serializable
@SerialName("circular")
private object Circular : ThemeAnimationFormat() {

    override fun toThemeAnimationFormat(): ComposeThemeAnimationFormat = ComposeThemeAnimationFormat.Circular
}

@Serializable
@SerialName("sliding")
private object Sliding : ThemeAnimationFormat() {

    override fun toThemeAnimationFormat(): ComposeThemeAnimationFormat = ComposeThemeAnimationFormat.Sliding
}

@Serializable
@SerialName("circularAroundPress")
private object CircularAroundPress : ThemeAnimationFormat() {

    override fun toThemeAnimationFormat(): ComposeThemeAnimationFormat = ComposeThemeAnimationFormat.CircularAroundPress
}

private class ThemeAnimationFormatSerializer :
    MapTransformingPolymorphicSerializer<ThemeAnimationFormat>(
        baseClass = ThemeAnimationFormat::class,
        subclasses = mapOf(
            Crossfade::class to Crossfade.serializer(),
            Circular::class to Circular.serializer(),
            Sliding::class to Sliding.serializer(),
            CircularAroundPress::class to CircularAroundPress.serializer(),
        ),
    )

public class ComposeThemeAnimationFormatSerializer : KSerializer<ComposeThemeAnimationFormat> {

    private val delegate = ThemeAnimationFormat.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: ComposeThemeAnimationFormat): Unit =
        encoder.encodeSerializableValue(
            delegate,
            when (value) {
                is ComposeThemeAnimationFormat.Crossfade -> Crossfade
                is ComposeThemeAnimationFormat.Circular -> Circular
                is ComposeThemeAnimationFormat.Sliding -> Sliding
                is ComposeThemeAnimationFormat.CircularAroundPress -> CircularAroundPress

                else -> throw IllegalArgumentException("Unknown easing '$value'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeThemeAnimationFormat =
        decoder.decodeSerializableValue(delegate).toThemeAnimationFormat()
}

public typealias ThemeAnimationFormatSerial = @Serializable(with = ComposeThemeAnimationFormatSerializer::class) ComposeThemeAnimationFormat
