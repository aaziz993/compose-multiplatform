package klib.data.config.http.client

import klib.data.config.EnabledConfig
import kotlinx.serialization.Serializable

@Serializable
public data class CacheConfig(
    val isShared: Boolean? = null,
    override val enabled: Boolean = true,
) : EnabledConfig
