package clib.presentation.theme.typography

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.material3.Typography as ComposeTypography

@Serializable
private data class Typography(
    val displayLarge: TextStyleSerial,
    val displayMedium: TextStyleSerial,
    val displaySmall: TextStyleSerial,
    val headlineLarge: TextStyleSerial,
    val headlineMedium: TextStyleSerial,
    val headlineSmall: TextStyleSerial,
    val titleLarge: TextStyleSerial,
    val titleMedium: TextStyleSerial,
    val titleSmall: TextStyleSerial,
    val bodyLarge: TextStyleSerial,
    val bodyMedium: TextStyleSerial,
    val bodySmall: TextStyleSerial,
    val labelLarge: TextStyleSerial,
    val labelMedium: TextStyleSerial,
    val labelSmall: TextStyleSerial,
    val displayLargeEmphasized: TextStyleSerial,
    val displayMediumEmphasized: TextStyleSerial,
    val displaySmallEmphasized: TextStyleSerial,
    val headlineLargeEmphasized: TextStyleSerial,
    val headlineMediumEmphasized: TextStyleSerial,
    val headlineSmallEmphasized: TextStyleSerial,
    val titleLargeEmphasized: TextStyleSerial,
    val titleMediumEmphasized: TextStyleSerial,
    val titleSmallEmphasized: TextStyleSerial,
    val bodyLargeEmphasized: TextStyleSerial,
    val bodyMediumEmphasized: TextStyleSerial,
    val bodySmallEmphasized: TextStyleSerial,
    val labelLargeEmphasized: TextStyleSerial,
    val labelMediumEmphasized: TextStyleSerial,
    val labelSmallEmphasized: TextStyleSerial,
)

public object TypographySerializer : KSerializer<ComposeTypography> {

    override val descriptor: SerialDescriptor = Typography.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ComposeTypography): Unit =
        encoder.encodeSerializableValue(
            Typography.serializer(),
            Typography(
                value.displayLarge,
                value.displayMedium,
                value.displaySmall,
                value.headlineLarge,
                value.headlineMedium,
                value.headlineSmall,
                value.titleLarge,
                value.titleMedium,
                value.titleSmall,
                value.bodyLarge,
                value.bodyMedium,
                value.bodySmall,
                value.labelLarge,
                value.labelMedium,
                value.labelSmall,
                value.displayLargeEmphasized,
                value.displayMediumEmphasized,
                value.displaySmallEmphasized,
                value.headlineLargeEmphasized,
                value.headlineMediumEmphasized,
                value.headlineSmallEmphasized,
                value.titleLargeEmphasized,
                value.titleMediumEmphasized,
                value.titleSmallEmphasized,
                value.bodyLargeEmphasized,
                value.bodyMediumEmphasized,
                value.bodySmallEmphasized,
                value.labelLargeEmphasized,
                value.labelMediumEmphasized,
                value.labelSmallEmphasized,
            ),
        )

    override fun deserialize(decoder: Decoder): ComposeTypography =
        decoder.decodeSerializableValue(Typography.serializer()).let { value ->
            ComposeTypography(
                value.displayLarge,
                value.displayMedium,
                value.displaySmall,
                value.headlineLarge,
                value.headlineMedium,
                value.headlineSmall,
                value.titleLarge,
                value.titleMedium,
                value.titleSmall,
                value.bodyLarge,
                value.bodyMedium,
                value.bodySmall,
                value.labelLarge,
                value.labelMedium,
                value.labelSmall,
                value.displayLargeEmphasized,
                value.displayMediumEmphasized,
                value.displaySmallEmphasized,
                value.headlineLargeEmphasized,
                value.headlineMediumEmphasized,
                value.headlineSmallEmphasized,
                value.titleLargeEmphasized,
                value.titleMediumEmphasized,
                value.titleSmallEmphasized,
                value.bodyLargeEmphasized,
                value.bodyMediumEmphasized,
                value.bodySmallEmphasized,
                value.labelLargeEmphasized,
                value.labelMediumEmphasized,
                value.labelSmallEmphasized,
            )
        }
}

public typealias TypographySerial = @Serializable(with = TypographySerializer::class) ComposeTypography
