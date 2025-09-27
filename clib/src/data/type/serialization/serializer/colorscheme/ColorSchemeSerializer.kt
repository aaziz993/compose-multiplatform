package data.type.serialization.serializer.colorscheme

import androidx.compose.material3.ColorScheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import ui.theme.lightColorScheme

private val defaultColors = listOf(
    lightColorScheme.primary,
    lightColorScheme.onPrimary,
    lightColorScheme.primaryContainer,
    lightColorScheme.onPrimaryContainer,
    lightColorScheme.inversePrimary,
    lightColorScheme.secondary,
    lightColorScheme.onSecondary,
    lightColorScheme.secondaryContainer,
    lightColorScheme.onSecondaryContainer,
    lightColorScheme.tertiary,
    lightColorScheme.onTertiary,
    lightColorScheme.tertiaryContainer,
    lightColorScheme.onTertiaryContainer,
    lightColorScheme.background,
    lightColorScheme.onBackground,
    lightColorScheme.surface,
    lightColorScheme.onSurface,
    lightColorScheme.surfaceVariant,
    lightColorScheme.onSurfaceVariant,
    lightColorScheme.surfaceTint,
    lightColorScheme.inverseSurface,
    lightColorScheme.inverseOnSurface,
    lightColorScheme.error,
    lightColorScheme.onError,
    lightColorScheme.errorContainer,
    lightColorScheme.onErrorContainer,
    lightColorScheme.outline,
    lightColorScheme.outlineVariant,
    lightColorScheme.scrim,
    lightColorScheme.surfaceBright,
    lightColorScheme.surfaceDim,
    lightColorScheme.surfaceContainer,
    lightColorScheme.surfaceContainerHigh,
    lightColorScheme.surfaceContainerHighest,
    lightColorScheme.surfaceContainerLow,
    lightColorScheme.surfaceContainerLowest,
)

public object ColorSchemeJsonSerializer : KSerializer<ColorScheme> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ColorScheme") {
        element("primary", ColorSerializer.descriptor)
        element("onPrimary", ColorSerializer.descriptor)
        element("primaryContainer", ColorSerializer.descriptor)
        element("onPrimaryContainer", ColorSerializer.descriptor)
        element("inversePrimary", ColorSerializer.descriptor)
        element("secondary", ColorSerializer.descriptor)
        element("onSecondary", ColorSerializer.descriptor)
        element("secondaryContainer", ColorSerializer.descriptor)
        element("onSecondaryContainer", ColorSerializer.descriptor)
        element("tertiary", ColorSerializer.descriptor)
        element("onTertiary", ColorSerializer.descriptor)
        element("tertiaryContainer", ColorSerializer.descriptor)
        element("onTertiaryContainer", ColorSerializer.descriptor)
        element("background", ColorSerializer.descriptor)
        element("onBackground", ColorSerializer.descriptor)
        element("surface", ColorSerializer.descriptor)
        element("onSurface", ColorSerializer.descriptor)
        element("surfaceVariant", ColorSerializer.descriptor)
        element("onSurfaceVariant", ColorSerializer.descriptor)
        element("surfaceTint", ColorSerializer.descriptor)
        element("inverseSurface", ColorSerializer.descriptor)
        element("inverseOnSurface", ColorSerializer.descriptor)
        element("error", ColorSerializer.descriptor)
        element("onError", ColorSerializer.descriptor)
        element("errorContainer", ColorSerializer.descriptor)
        element("onErrorContainer", ColorSerializer.descriptor)
        element("outline", ColorSerializer.descriptor)
        element("outlineVariant", ColorSerializer.descriptor)
        element("scrim", ColorSerializer.descriptor)
        element("surfaceBright", ColorSerializer.descriptor)
        element("surfaceDim", ColorSerializer.descriptor)
        element("surfaceContainer", ColorSerializer.descriptor)
        element("surfaceContainerHigh", ColorSerializer.descriptor)
        element("surfaceContainerHighest", ColorSerializer.descriptor)
        element("surfaceContainerLow", ColorSerializer.descriptor)
        element("surfaceContainerLowest", ColorSerializer.descriptor)
    }

    override fun serialize(encoder: Encoder, value: ColorScheme): Unit = with(value) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, ColorSerializer, primary)
            encodeSerializableElement(descriptor, 1, ColorSerializer, onPrimary)
            encodeSerializableElement(descriptor, 2, ColorSerializer, primaryContainer)
            encodeSerializableElement(descriptor, 3, ColorSerializer, onPrimaryContainer)
            encodeSerializableElement(descriptor, 4, ColorSerializer, inversePrimary)
            encodeSerializableElement(descriptor, 5, ColorSerializer, secondary)
            encodeSerializableElement(descriptor, 6, ColorSerializer, onSecondary)
            encodeSerializableElement(descriptor, 7, ColorSerializer, secondaryContainer)
            encodeSerializableElement(descriptor, 8, ColorSerializer, onSecondaryContainer)
            encodeSerializableElement(descriptor, 9, ColorSerializer, tertiary)
            encodeSerializableElement(descriptor, 10, ColorSerializer, onTertiary)
            encodeSerializableElement(descriptor, 11, ColorSerializer, tertiaryContainer)
            encodeSerializableElement(descriptor, 12, ColorSerializer, onTertiaryContainer)
            encodeSerializableElement(descriptor, 13, ColorSerializer, background)
            encodeSerializableElement(descriptor, 14, ColorSerializer, onBackground)
            encodeSerializableElement(descriptor, 15, ColorSerializer, surface)
            encodeSerializableElement(descriptor, 16, ColorSerializer, onSurface)
            encodeSerializableElement(descriptor, 17, ColorSerializer, surfaceVariant)
            encodeSerializableElement(descriptor, 18, ColorSerializer, onSurfaceVariant)
            encodeSerializableElement(descriptor, 19, ColorSerializer, surfaceTint)
            encodeSerializableElement(descriptor, 20, ColorSerializer, inverseSurface)
            encodeSerializableElement(descriptor, 21, ColorSerializer, inverseOnSurface)
            encodeSerializableElement(descriptor, 22, ColorSerializer, error)
            encodeSerializableElement(descriptor, 23, ColorSerializer, onError)
            encodeSerializableElement(descriptor, 24, ColorSerializer, errorContainer)
            encodeSerializableElement(descriptor, 25, ColorSerializer, onErrorContainer)
            encodeSerializableElement(descriptor, 26, ColorSerializer, outline)
            encodeSerializableElement(descriptor, 27, ColorSerializer, outlineVariant)
            encodeSerializableElement(descriptor, 28, ColorSerializer, scrim)
            encodeSerializableElement(descriptor, 29, ColorSerializer, surfaceBright)
            encodeSerializableElement(descriptor, 30, ColorSerializer, surfaceDim)
            encodeSerializableElement(descriptor, 31, ColorSerializer, surfaceContainer)
            encodeSerializableElement(descriptor, 32, ColorSerializer, surfaceContainerHigh)
            encodeSerializableElement(descriptor, 33, ColorSerializer, surfaceContainerHighest)
            encodeSerializableElement(descriptor, 34, ColorSerializer, surfaceContainerLow)
            encodeSerializableElement(descriptor, 35, ColorSerializer, surfaceContainerLowest)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): ColorScheme {
        return decoder.decodeStructure(descriptor) {
            val colors = defaultColors.toMutableList()

            if (decodeSequentially()) {
                for (index in 0..35) {
                    decodeNullableSerializableElement(descriptor, index, ColorSerializer)?.let { colors[index] = it }
                }
            }
            else {
                do {
                    val index = decodeElementIndex(descriptor)

                    if (index !in 0..35) error("Unexpected index: $index")

                    if (index == CompositeDecoder.DECODE_DONE) {
                        break
                    }

                    decodeNullableSerializableElement(descriptor, index, ColorSerializer)?.let { colors[index] = it }
                } while (true)
            }

            ColorScheme(
                colors[0],
                colors[1],
                colors[2],
                colors[3],
                colors[4],
                colors[5],
                colors[6],
                colors[7],
                colors[8],
                colors[9],
                colors[10],
                colors[11],
                colors[12],
                colors[13],
                colors[14],
                colors[15],
                colors[16],
                colors[17],
                colors[18],
                colors[19],
                colors[20],
                colors[21],
                colors[22],
                colors[23],
                colors[24],
                colors[25],
                colors[26],
                colors[27],
                colors[28],
                colors[29],
                colors[30],
                colors[31],
                colors[32],
                colors[33],
                colors[34],
                colors[35],
            )
        }
    }
}

public typealias ColorSchemeJson = @Serializable(with = ColorSchemeJsonSerializer::class) ColorScheme
