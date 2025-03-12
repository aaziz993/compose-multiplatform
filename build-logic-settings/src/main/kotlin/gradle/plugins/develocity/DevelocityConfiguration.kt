package gradle.plugins.develocity

import gradle.tryAssign
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
    fun applyTo() {
        buildScan?.let { buildScan ->
            develocity.buildScan {
                buildScan.applyTo(this)
            }
        }

        develocity.server tryAssign server
        develocity.edgeDiscovery tryAssign edgeDiscovery
        develocity.projectId tryAssign projectId
        develocity.allowUntrustedServer tryAssign allowUntrustedServer
        develocity.accessKey tryAssign accessKey
    }
}
