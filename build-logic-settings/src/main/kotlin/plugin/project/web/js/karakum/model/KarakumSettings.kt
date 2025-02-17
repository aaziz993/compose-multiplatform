package plugin.project.web.js.karakum.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KarakumSettings(
    override val configFile: String? = null,
    override val extensionSource: String? = null,
    val task: KarakumGenerate = KarakumGenerate(),
    val enabled: Boolean = true
) : KarakumExtension
