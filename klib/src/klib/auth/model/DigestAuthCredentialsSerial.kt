package klib.auth.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import io.ktor.client.plugins.auth.providers.DigestAuthCredentials as KtorDigestAuthCredentials

@Serializable
private data class DigestAuthCredentials(
    val username: String,
    val password: String,
)

public object DigestAuthCredentialsSerializer : KSerializer<KtorDigestAuthCredentials> {

    private val delegate = DigestAuthCredentials.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: KtorDigestAuthCredentials): Unit =
        encoder.encodeSerializableValue(
            delegate,
            DigestAuthCredentials(
                value.username,
                value.password,
            ),
        )

    override fun deserialize(decoder: Decoder): KtorDigestAuthCredentials =
        decoder.decodeSerializableValue(delegate).let { value ->
            KtorDigestAuthCredentials(
                value.username,
                value.password,
            )
        }
}

public typealias DigestAuthCredentialsSerial = @Serializable(with = DigestAuthCredentialsSerializer::class) KtorDigestAuthCredentials


