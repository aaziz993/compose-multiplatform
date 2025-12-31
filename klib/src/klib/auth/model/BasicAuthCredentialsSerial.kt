package klib.auth.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials as KtorBasicAuthCredentials

@Serializable
private data class BasicAuthCredentials(
    val username: String,
    val password: String,
)

public object BasicAuthCredentialsSerializer : KSerializer<KtorBasicAuthCredentials> {

    private val delegate = BasicAuthCredentials.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: KtorBasicAuthCredentials): Unit =
        encoder.encodeSerializableValue(
            delegate,
            BasicAuthCredentials(
                value.username,
                value.password,
            ),
        )

    override fun deserialize(decoder: Decoder): KtorBasicAuthCredentials =
        decoder.decodeSerializableValue(delegate).let { value ->
            KtorBasicAuthCredentials(
                value.username,
                value.password,
            )
        }
}

public typealias BasicAuthCredentialsSerial = @Serializable(with = BasicAuthCredentialsSerializer::class) KtorBasicAuthCredentials


