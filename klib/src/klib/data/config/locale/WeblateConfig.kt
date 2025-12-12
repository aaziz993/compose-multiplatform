package klib.data.config.locale

import klib.data.config.EnabledConfig
import kotlinx.serialization.Serializable

@Serializable
public data class WeblateConfig(
    val baseUrl: String,
    val apiKey: String,
    val projectName: String,
    val components: Set<String>,
    override val enabled: Boolean = true,
) : EnabledConfig
