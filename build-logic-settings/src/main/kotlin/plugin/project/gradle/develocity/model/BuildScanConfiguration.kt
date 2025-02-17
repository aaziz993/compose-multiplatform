package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanConfiguration(
    val background: BuildScanConfiguration? = null,
    val tag: String? = null,
    val values: Map<String, String>? = null,
    val links: List<Link>? = null,
    val termsOfUseUrl: String? = null,
    val termsOfUseAgree: String? = null,
    val uploadInBackground: Boolean? = null,
    val publishing: BuildScanPublishingConfiguration? = null,
    val obfuscation: BuildScanDataObfuscationConfiguration? = null,
    val capture: BuildScanCaptureConfiguration?
)
