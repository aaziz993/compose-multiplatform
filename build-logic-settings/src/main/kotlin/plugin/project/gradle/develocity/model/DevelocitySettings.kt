package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable
import plugin.settings.model.LocalBuildCache
import plugin.settings.model.RemoteBuildCache

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    val enabled: Boolean = true,
    val localCache: LocalBuildCache = LocalBuildCache(),
    val remoteCache: RemoteBuildCache = RemoteBuildCache(),
    val git: Git? = null,
) : DevelocityConfiguration
