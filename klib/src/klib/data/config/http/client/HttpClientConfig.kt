package klib.data.config.http.client

import klib.data.config.LogConfig
import kotlinx.serialization.Serializable

@Serializable
public data class HttpClientConfig(
    val log: LogConfig? = null,
    val timeout: TimeoutConfig? = null,
    val cache: CacheConfig? = null
)
