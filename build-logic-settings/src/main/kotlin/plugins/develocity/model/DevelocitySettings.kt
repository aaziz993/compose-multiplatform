package plugins.develocity.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.api.CI
import gradle.buildcache.DirectoryBuildCache
import gradle.buildcache.RemoteBuildCache
import gradle.plugins.develocity.BuildScanConfiguration
import gradle.plugins.develocity.DevelocityConfiguration
import gradle.plugins.develocity.Git
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

@Serializable
internal data class DevelocitySettings(
    override val buildScan: BuildScanConfiguration? = null,
    override val server: String? = null,
    override val edgeDiscovery: Boolean? = null,
    override val projectId: String? = null,
    override val allowUntrustedServer: Boolean? = null,
    override val accessKey: String? = null,
    override val enabled: Boolean = true,
    val localCache: DirectoryBuildCache? = null,
    val remoteCache: RemoteBuildCache? = null,
    val git: Git? = null,
) : DevelocityConfiguration, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
            super.applyTo()

            buildCache {
                localCache?.takeIf { CI }?.let { localCache ->
                    local {
                        localCache.applyTo(this)
                    }
                }
                remoteCache?.let { remoteCache ->
                    remote(develocity.buildCache) {
                        remoteCache.applyTo(this)

                        // Check access key presence to avoid build cache errors on PR builds when access key is not present
                        val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
                            projectProperties.gradleEnterpriseAccessKey
                        }

                        isPush = isPush && CI && accessKey != null
                    }
                }
            }
        }
}
