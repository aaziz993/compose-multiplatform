package plugin.project.gradle.buildconfig.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildConfigSettings(
    val enabled: Boolean = true,
    override val sourceSets: List<String>? = null,
) : BuildConfigExtension
