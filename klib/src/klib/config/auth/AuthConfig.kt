package klib.config.auth

import com.sunildhiman90.kmauth.core.KMAuthConfig
import com.sunildhiman90.kmauth.core.KMAuthInitializer
import com.sunildhiman90.kmauth.supabase.KMAuthSupabase
import klib.auth.model.AuthResource
import klib.auth.otp.model.OtpConfig
import klib.auth.otp.model.TotpConfig
import klib.config.auth.oauth.SupabaseOAuthConfig
import kotlinx.serialization.Serializable

@Serializable
public data class AuthConfig(
    val components: Map<String?, AuthResource> = mapOf(null to AuthResource()),
    val pinCode: PinCodeConfig = PinCodeConfig(),
    val password: PasswordConfig = PasswordConfig(),
    val otp: OtpConfig = TotpConfig(),
    val google: GoogleAuthProviderConfig? = null,
    val supabase: SupabaseAuthProviderConfig? = null,
    val supabaseDefaultAuths: List<SupabaseDefaultAuthConfig> = emptyList(),
    val supabaseOAuths: List<SupabaseOAuthConfig> = emptyList(),
) {

    public fun configure() {
        google?.let {
            KMAuthInitializer.initialize(
                KMAuthConfig.forGoogle(webClientId = it.webClientId, clientSecret = it.clientSecret),
            )
        }
        supabase?.let {
            KMAuthSupabase.initialize(it.config, it.redirectUrl)
        }
    }
}
