package plugin.project.web.js.karakum.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KarakumSettings(
    val configFile: String? = null, // config file path relative to project
)
