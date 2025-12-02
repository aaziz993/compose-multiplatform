package klib.data.config

import kotlinx.serialization.Serializable

@Serializable
public data class ApplicationConfig(
    val name: String,
    val environment: String = "development",
)
