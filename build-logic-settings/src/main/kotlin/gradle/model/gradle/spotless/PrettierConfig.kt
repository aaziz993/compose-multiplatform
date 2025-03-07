package gradle.model.gradle.spotless

import kotlinx.serialization.Serializable

@Serializable
internal data class PrettierConfig(
    val devDependencies: MutableMap<String, String>? = null
)
