package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NativeCmd(
    val name: String,
    val pathToExe: String,
    val arguments: List<String> = emptyList()
)
