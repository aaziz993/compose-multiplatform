package klib.config.auth

import com.sunildhiman90.kmauth.core.KMAuthPlatformContext
import com.sunildhiman90.kmauth.core.KMAuthSupabaseFlowType
import com.sunildhiman90.kmauth.supabase.model.PhoneConfirmationChannel
import com.sunildhiman90.kmauth.core.KMAuthConfig as KMAuthKMAuthConfig
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
private data class KMAuthConfig(
    val providerId: String = "default",
    val webClientId: String? = null,
    val clientSecret: String? = null,
    val supabaseUrl: String? = null,
    val supabaseKey: String? = null,
    val autoLoadFromStorage: Boolean = true,
    val autoRefreshToken: Boolean = true,
    val deepLinkHost: String? = null,
    val deepLinkScheme: String? = null,
    val flowType: KMAuthSupabaseFlowType? = null
)

public object KMAuthConfigSerializer : KSerializer<KMAuthKMAuthConfig> {

    private val delegate = KMAuthConfig.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: KMAuthKMAuthConfig): Unit =
        encoder.encodeSerializableValue(
            delegate,
            KMAuthConfig(
                providerId = value.providerId,
                webClientId = value.webClientId,
                clientSecret = value.clientSecret,
                supabaseUrl = value.supabaseUrl,
                supabaseKey = value.supabaseKey,
                autoLoadFromStorage = value.autoLoadFromStorage,
                autoRefreshToken = value.autoRefreshToken,
                deepLinkHost = value.deepLinkHost,
                deepLinkScheme = value.deepLinkScheme,
                flowType = value.flowType,
            ),
        )

    override fun deserialize(decoder: Decoder): KMAuthKMAuthConfig =
        decoder.decodeSerializableValue(delegate).let { value ->
            KMAuthKMAuthConfig(
                providerId = value.providerId,
                webClientId = value.webClientId,
                clientSecret = value.clientSecret,
                supabaseUrl = value.supabaseUrl,
                supabaseKey = value.supabaseKey,
                autoLoadFromStorage = value.autoLoadFromStorage,
                autoRefreshToken = value.autoRefreshToken,
                deepLinkHost = value.deepLinkHost,
                deepLinkScheme = value.deepLinkScheme,
                flowType = value.flowType,
            )
        }
}

public typealias KMAuthConfigSerial = @Serializable(with = KMAuthConfigSerializer::class) KMAuthKMAuthConfig
