package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class EclipseConfig(
    val formatterVersion: String?=null,
    val settingsFiles: List<String>?=null,
    val p2Mirrors: Map<String, String> ?=null
)

