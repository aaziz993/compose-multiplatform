package gradle.plugins.develocity

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.plugins.develocity.buildscan.BuildScanConfiguration
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

internal interface DevelocityConfiguration {

    val buildScan: BuildScanConfiguration?
    val server: String?
    val edgeDiscovery: Boolean?
    val projectId: String?
    val allowUntrustedServer: Boolean?
    val accessKey: String?

    context(Settings)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("develocity").id) {
            buildScan?.applyTo(develocity.buildScan)
            develocity.server tryAssign server
            develocity.edgeDiscovery tryAssign edgeDiscovery
            develocity.projectId tryAssign projectId
            develocity.allowUntrustedServer tryAssign allowUntrustedServer
            develocity.accessKey tryAssign accessKey
        }
}
