package klib.data.auth.oauth

import com.sunildhiman90.kmauth.supabase.model.PhoneConfirmationChannel
import com.sunildhiman90.kmauth.supabase.model.SupabaseAuthConfig as KMAuthSupabaseAuthConfig
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
private data class SupabaseAuthConfig(
    val supabaseUrl: String? = null,
    val scopes: List<String> = emptyList(),
    val queryParams: Map<String, String> = emptyMap(),
    val automaticallyOpenUrl: Boolean = true,
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val channel: PhoneConfirmationChannel = PhoneConfirmationChannel.SMS,
    val idToken: String = "",
    val provider: SupabaseOAuthProvider? = null,
    val accessToken: String? = null,
    val nonce: String? = null,
)

public object SupabaseAuthConfigSerializer : KSerializer<KMAuthSupabaseAuthConfig> {

    private val delegate = SupabaseAuthConfig.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: KMAuthSupabaseAuthConfig): Unit =
        encoder.encodeSerializableValue(
            delegate,
            SupabaseAuthConfig(
                value.supabaseUrl,
                value.scopes,
                value.queryParams,
                value.automaticallyOpenUrl,
                value.email,
                value.phone,
                value.password,
                value.channel,
                value.idToken,
                value.provider,
                value.accessToken,
                value.nonce,
            ),
        )

    override fun deserialize(decoder: Decoder): KMAuthSupabaseAuthConfig =
        decoder.decodeSerializableValue(delegate).let { value ->
            KMAuthSupabaseAuthConfig().apply {
                supabaseUrl = value.supabaseUrl
                scopes = value.scopes.toMutableList()
                queryParams = value.queryParams.toMutableMap()
                automaticallyOpenUrl = value.automaticallyOpenUrl
                email = value.email
                phone = value.phone
                password = value.password
                channel = value.channel
                idToken = value.idToken
                provider = value.provider
                accessToken = value.accessToken
                nonce = value.nonce
            }
        }
}

public typealias SupabaseAuthConfigSerial = @Serializable(with = SupabaseAuthConfigSerializer::class) KMAuthSupabaseAuthConfig
