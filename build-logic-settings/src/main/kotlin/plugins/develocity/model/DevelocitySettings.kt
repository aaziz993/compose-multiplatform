package plugins.develocity.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.api.CI
import gradle.caching.RemoteBuildCache
import gradle.plugins.develocity.BuildScanConfiguration
import gradle.plugins.develocity.DevelocityConfiguration
import gradle.plugins.develocity.Git
import gradle.project.EnabledSettings
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
    val remoteCache: RemoteBuildCache? = null,
    val git: Git? = null,
) : DevelocityConfiguration, EnabledSettings {

    context(Settings)
    override fun applyTo() =
        pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
            super.applyTo()

            buildCache {
                remoteCache?.let { remoteCache ->
                    remote(develocity.buildCache) {
                        remoteCache.applyTo(this)

                        // Check access key presence to avoid build cache errors on PR builds when access key is not present
                        isPush = isPush && CI && projectProperties.gradleEnterpriseAccessKey?.resolveValue() != null
                    }
                }
            }
        }
}
