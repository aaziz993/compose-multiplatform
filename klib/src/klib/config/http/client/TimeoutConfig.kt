package klib.config.http.client

import klib.config.EnabledConfig
import kotlinx.serialization.Serializable

@Serializable
public data class TimeoutConfig(
    val requestTimeoutMillis: Long? = null,
    val connectTimeoutMillis: Long? = null,
    val socketTimeoutMillis: Long? = null,
    override val enabled: Boolean = true
) : EnabledConfig
