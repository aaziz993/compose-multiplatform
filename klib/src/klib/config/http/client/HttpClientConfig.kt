package klib.config.http.client

import klib.config.LogConfig
import kotlinx.serialization.Serializable

@Serializable
public data class HttpClientConfig(
    val log: LogConfig? = null,
    val timeout: TimeoutConfig? = null,
    val cache: CacheConfig? = null
)
