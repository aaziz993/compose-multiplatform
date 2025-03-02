package plugin.project.gradle.develocity.model

import gradle.id
import gradle.isCI
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import plugin.project.gradle.model.DirectoryBuildCache
import plugin.project.gradle.model.RemoteBuildCache
import plugin.project.model.EnabledSettings

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    override val enabled: Boolean = true,
    val localCache: DirectoryBuildCache = DirectoryBuildCache(),
    val remoteCache: RemoteBuildCache = RemoteBuildCache(),
    val git: Git? = null,
) : DevelocityConfiguration, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
            super.applyTo()


            if (isCI) {
                buildCache {
                    localCache.let { localCache ->
                        local {
                            localCache.applyTo(this)
                        }
                    }
                    remoteCache.let { remoteCache ->
                        remote(develocity.buildCache) {
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
