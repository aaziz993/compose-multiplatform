package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ClangFormatConfig(
    val version: String? = null,
    val pathToExe: String? = null,
    val style: String? = null,
)
