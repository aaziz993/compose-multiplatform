package klib.data.config.auth.oauth

import com.sunildhiman90.kmauth.supabase.model.SupabaseAuthConfig
import com.sunildhiman90.kmauth.supabase.model.SupabaseDefaultAuthProvider
import klib.data.config.auth.SupabaseAuthConfigSerial
import kotlinx.serialization.Serializable

@Serializable
public data class SupabaseDefaultAuthConfig(
    val provider: SupabaseDefaultAuthProvider,
    val config: SupabaseAuthConfigSerial = SupabaseAuthConfig(),
)
