package plugin.project.gradle.develocity.model

internal interface DevelocityConfiguration {

    val buildScan: BuildScanConfiguration?
    val server: String?
    val edgeDiscovery: Boolean?
    val projectId: String?
    val allowUntrustedServer: Boolean?
    val accessKey: String?
}
