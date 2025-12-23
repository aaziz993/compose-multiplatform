package klib.data.config.auth.oauth

import com.sunildhiman90.kmauth.supabase.model.SupabaseAuthConfig
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import klib.data.config.auth.SupabaseAuthConfigSerial
import kotlinx.serialization.Serializable

@Serializable
public data class SupabaseOAuthConfig(
    val provider: SupabaseOAuthProvider,
    val config: SupabaseAuthConfigSerial = SupabaseAuthConfig(),
)
