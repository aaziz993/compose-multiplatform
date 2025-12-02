package klib.data.config.server

import kotlinx.serialization.Serializable

@Serializable
public data class ServerConfig(
    val host: String,
    val port: Int
)
