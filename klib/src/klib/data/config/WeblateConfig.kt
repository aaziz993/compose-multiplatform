package klib.data.config

import kotlinx.serialization.Serializable

@Serializable
internal data class WeblateConfig(
    val address: String,
    val apiKey: String,
    override val enabled: Boolean = true,
) : EnabledConfig
