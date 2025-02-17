package plugin.project.web.js.karakum.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KarakumGenerate(
    val configFile: String? = null,
    val extensionDirectory: String? = null,
)
