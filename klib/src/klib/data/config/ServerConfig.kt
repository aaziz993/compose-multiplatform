package klib.data.config

import kotlinx.serialization.Serializable

@Serializable
public data class ServerConfig(
    val host: String = "127.0.0.1",
    public val port: Int = 80,
    public val sslPort: Int = 443,
)
