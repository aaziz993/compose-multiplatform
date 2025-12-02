package clib.presentation.theme.typography

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.material3.Typography as ComposeTypography

private val TYPOGRAPHY = ComposeTypography()

@Serializable
private data class Typography(
    val displayLarge: TextStyleSerial = TYPOGRAPHY.displayLarge,
    val displayMedium: TextStyleSerial = TYPOGRAPHY.displayMedium,
    val displaySmall: TextStyleSerial = TYPOGRAPHY.displaySmall,
    val headlineLarge: TextStyleSerial = TYPOGRAPHY.headlineLarge,
    val headlineMedium: TextStyleSerial = TYPOGRAPHY.headlineMedium,
    val headlineSmall: TextStyleSerial = TYPOGRAPHY.headlineSmall,
    val titleLarge: TextStyleSerial = TYPOGRAPHY.titleLarge,
    val titleMedium: TextStyleSerial = TYPOGRAPHY.titleMedium,
    val titleSmall: TextStyleSerial = TYPOGRAPHY.titleSmall,
    val bodyLarge: TextStyleSerial = TYPOGRAPHY.bodyLarge,
    val bodyMedium: TextStyleSerial = TYPOGRAPHY.bodyMedium,
    val bodySmall: TextStyleSerial = TYPOGRAPHY.bodySmall,
    val labelLarge: TextStyleSerial = TYPOGRAPHY.labelLarge,
    val labelMedium: TextStyleSerial = TYPOGRAPHY.labelMedium,
    val labelSmall: TextStyleSerial = TYPOGRAPHY.labelSmall,
    val displayLargeEmphasized: TextStyleSerial = TYPOGRAPHY.displayLargeEmphasized,
    val displayMediumEmphasized: TextStyleSerial = TYPOGRAPHY.displayMediumEmphasized,
    val displaySmallEmphasized: TextStyleSerial = TYPOGRAPHY.displaySmallEmphasized,
    val headlineLargeEmphasized: TextStyleSerial = TYPOGRAPHY.headlineLargeEmphasized,
    val headlineMediumEmphasized: TextStyleSerial = TYPOGRAPHY.headlineMediumEmphasized,
    val headlineSmallEmphasized: TextStyleSerial = TYPOGRAPHY.headlineSmallEmphasized,
    val titleLargeEmphasized: TextStyleSerial = TYPOGRAPHY.titleLargeEmphasized,
    val titleMediumEmphasized: TextStyleSerial = TYPOGRAPHY.titleMediumEmphasized,
    val titleSmallEmphasized: TextStyleSerial = TYPOGRAPHY.titleSmallEmphasized,
    val bodyLargeEmphasized: TextStyleSerial = TYPOGRAPHY.bodyLargeEmphasized,
    val bodyMediumEmphasized: TextStyleSerial = TYPOGRAPHY.bodyMediumEmphasized,
    val bodySmallEmphasized: TextStyleSerial = TYPOGRAPHY.bodySmallEmphasized,
    val labelLargeEmphasized: TextStyleSerial = TYPOGRAPHY.labelLargeEmphasized,
    val labelMediumEmphasized: TextStyleSerial = TYPOGRAPHY.labelMediumEmphasized,
    val labelSmallEmphasized: TextStyleSerial = TYPOGRAPHY.labelSmallEmphasized,
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
