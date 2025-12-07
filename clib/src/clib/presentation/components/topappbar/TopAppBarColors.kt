package clib.presentation.components.topappbar

import clib.data.type.ColorSerial
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.material3.TopAppBarColors as ComposeTopAppBarColors

@Serializable
private data class TopAppBarColors(
    val containerColor: ColorSerial,
    val scrolledContainerColor: ColorSerial,
    val navigationIconContentColor: ColorSerial,
    val titleContentColor: ColorSerial,
    val actionIconContentColor: ColorSerial,
    val subtitleContentColor: ColorSerial,
)

public object ComposeTopAppBarColorsSerializer : KSerializer<ComposeTopAppBarColors> {

    private val delegate = TopAppBarColors.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ComposeTopAppBarColors): Unit =
        encoder.encodeSerializableValue(
            delegate,
            TopAppBarColors(
                value.containerColor,
                value.scrolledContainerColor,
                value.navigationIconContentColor,
                value.titleContentColor,
                value.actionIconContentColor,
                value.subtitleContentColor,
            ),
        )

    override fun deserialize(decoder: Decoder): ComposeTopAppBarColors =
        decoder.decodeSerializableValue(delegate).let { value ->
            ComposeTopAppBarColors(
                value.containerColor,
                value.scrolledContainerColor,
                value.navigationIconContentColor,
                value.titleContentColor,
                value.actionIconContentColor,
                value.subtitleContentColor,
            )
        }
}

public typealias TopAppBarColorsSerial = @Serializable(with = ComposeTopAppBarColorsSerializer::class) ComposeTopAppBarColors
