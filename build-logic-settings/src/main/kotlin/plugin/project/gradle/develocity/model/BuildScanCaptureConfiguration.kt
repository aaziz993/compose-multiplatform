package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanCaptureConfiguration(
    val fileFingerprints: Boolean? = null,
    val buildLogging: Boolean? = null,
    val testLogging: Boolean? = null,
    val resourceUsage: Boolean? = null,
)
