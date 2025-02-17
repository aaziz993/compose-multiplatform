package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanDataObfuscationConfiguration(
    val username: Map<String, String>? = null,
    val hostname: Map<String, String>? = null,
    val ipAddresses: Map<String, String>? = null,
    val externalProcessName: Map<String, String>? = null,
)
