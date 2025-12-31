package klib.config.http.client

import klib.config.EnabledConfig
import kotlinx.serialization.Serializable

@Serializable
public data class CacheConfig(
    val isShared: Boolean? = null,
    override val enabled: Boolean = true,
) : EnabledConfig
