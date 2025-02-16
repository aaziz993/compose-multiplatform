package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class PrettierConfig(
    val devDependencies: MutableMap<String, String>? = null
)
