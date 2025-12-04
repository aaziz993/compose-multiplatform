@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.presentation.theme.typography

import androidx.compose.material3.tokens.DefaultTextStyle
import clib.data.type.SpSerial
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.ui.text.TextStyle as ComposeTextStyle

@Serializable
private data class TextStyle(
    val fontSize: SpSerial,
    val fontWeight: FontWeightSerial?,
    val fontStyle: FontStyleSerial?,
    val fontSynthesis: FontSynthesisSerial?,
    val fontFamily: FontFamilySerial?,
    val lineHeight: SpSerial,
    val letterSpacing: SpSerial,
)

public object TextStyleSerializer : KSerializer<ComposeTextStyle> {

    private val delegate = TextStyle.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ComposeTextStyle): Unit =
        encoder.encodeSerializableValue(
            delegate,
            TextStyle(
                fontSize = value.fontSize,
                fontWeight = value.fontWeight,
                fontStyle = value.fontStyle,
                fontSynthesis = value.fontSynthesis,
                fontFamily = value.fontFamily,
                lineHeight = value.lineHeight,
                letterSpacing = value.letterSpacing,
            ),
        )

    override fun deserialize(decoder: Decoder): ComposeTextStyle =
        decoder.decodeSerializableValue(delegate).let { value ->
            DefaultTextStyle.copy(
                fontSize = value.fontSize,
                fontWeight = value.fontWeight,
                fontStyle = value.fontStyle,
                fontSynthesis = value.fontSynthesis,
                fontFamily = value.fontFamily,
                lineHeight = value.lineHeight,
                letterSpacing = value.letterSpacing,
            )
        }
}

public typealias TextStyleSerial = @Serializable(with = TextStyleSerializer::class) ComposeTextStyle

