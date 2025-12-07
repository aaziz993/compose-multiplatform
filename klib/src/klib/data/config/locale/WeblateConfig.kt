package klib.data.config.locale

import klib.data.config.EnabledConfig
import kotlinx.serialization.Serializable

@Serializable
internal data class WeblateConfig(
    val baseUrl: String,
    val apiKey: String,
    override val enabled: Boolean = true,
) : EnabledConfig
