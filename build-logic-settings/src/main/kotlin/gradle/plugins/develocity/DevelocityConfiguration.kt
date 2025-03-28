package gradle.plugins.develocity

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
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
        settings.pluginManager.withPlugin(settings.libs.plugin("develocity").id) {
            buildScan?.applyTo(settings.develocity.buildScan)
            settings.develocity.server tryAssign server
            settings.develocity.edgeDiscovery tryAssign edgeDiscovery
            settings.develocity.projectId tryAssign projectId
            settings.develocity.allowUntrustedServer tryAssign allowUntrustedServer
            settings.develocity.accessKey tryAssign accessKey
        }
}
