package plugin.project.gradle.develocity.model

import gradle.isCI
import gradle.projectProperties
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import plugin.project.gradle.model.DirectoryBuildCache
import plugin.project.gradle.model.RemoteBuildCache

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    val enabled: Boolean = true,
    val localCache: DirectoryBuildCache = DirectoryBuildCache(),
    val remoteCache: RemoteBuildCache = RemoteBuildCache(),
    val git: Git? = null,
) : DevelocityConfiguration {

    context(Settings)
    fun applyTo(configuration: com.gradle.develocity.agent.gradle.DevelocityConfiguration) {
        buildScan?.let { buildScan ->
            configuration.buildScan {
                buildScan.applyTo(this)
            }
        }

        configuration.server tryAssign server
        configuration.edgeDiscovery tryAssign edgeDiscovery
        configuration.projectId tryAssign projectId
        configuration.allowUntrustedServer tryAssign allowUntrustedServer
        configuration.accessKey tryAssign accessKey

        if (isCI) {
            buildCache {
                localCache.let { localCache ->
                    local {
                        localCache.applyTo(this)
                    }
                }
                remoteCache.let { remoteCache ->
                    remote(configuration.buildCache) {
                        remoteCache.applyTo(this)

                        // Check access key presence to avoid build cache errors on PR builds when access key is not present
                        val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
                           projectProperties.gradleEnterpriseAccessKey
                        }

                        isPush = remoteCache.isPush == true && accessKey != null
                    }
                }
            }
        }
    }
}
