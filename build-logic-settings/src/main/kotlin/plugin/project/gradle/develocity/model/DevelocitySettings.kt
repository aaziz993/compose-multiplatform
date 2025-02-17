package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    val enabled: Boolean = true,
    val localBuildCache: Boolean=true,
    val remoteBuildCache: Boolean=true,
) : DevelocityConfiguration
