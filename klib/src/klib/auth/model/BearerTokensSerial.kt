package klib.auth.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import io.ktor.client.plugins.auth.providers.BearerTokens as KtorBearerTokens

@Serializable
private data class BearerTokens(
    val accessToken: String,
    val refreshToken: String?
)

public object BearerTokensSerializer : KSerializer<KtorBearerTokens> {

    private val delegate = BearerTokens.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: KtorBearerTokens): Unit =
        encoder.encodeSerializableValue(
            delegate,
            BearerTokens(
                value.accessToken,
                value.refreshToken,
            ),
        )

    override fun deserialize(decoder: Decoder): KtorBearerTokens =
        decoder.decodeSerializableValue(delegate).let { value ->
            KtorBearerTokens(
                value.accessToken,
                value.refreshToken,
            )
        }
}

public typealias BearerTokensSerial = @Serializable(with = BearerTokensSerializer::class) KtorBearerTokens


