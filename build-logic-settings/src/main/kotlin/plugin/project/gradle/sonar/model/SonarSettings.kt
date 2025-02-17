package plugin.project.gradle.sonar.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SonarSettings(
    override val skipProject: Boolean? = null,
    override val properties: Map<String, String>? = null,
    override var androidVariant: String? = null,
    val enabled: Boolean = true
) : SonarExtension
