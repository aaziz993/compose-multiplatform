package klib.data.config.auth

import com.sunildhiman90.kmauth.core.KMAuthSupabaseFlowType
import kotlinx.serialization.Serializable

@Serializable
public data class SupabaseAuthProviderConfig(
    val supabaseUrl: String,
    val supabaseKey: String,
    val autoLoadFromStorage: Boolean = true,
    val autoRefreshToken: Boolean = true,
    val deepLinkHost: String? = null,
    val deepLinkScheme: String? = null,
    val kmAuthSupabaseFlowType: KMAuthSupabaseFlowType? = null
)
