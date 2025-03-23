package gradle.plugins.develocity.buildscan

import com.gradle.develocity.agent.gradle.scan.BuildScanCaptureConfiguration
import gradle.api.tryAssign
import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanCaptureConfiguration(
    val fileFingerprints: Boolean? = null,
    val buildLogging: Boolean? = null,
    val testLogging: Boolean? = null,
    val resourceUsage: Boolean? = null,
) {

    fun applyTo(receiver: BuildScanCaptureConfiguration) {
        receiver.fileFingerprints tryAssign fileFingerprints
        receiver.buildLogging tryAssign buildLogging
        receiver.testLogging tryAssign testLogging
        receiver.resourceUsage tryAssign resourceUsage
    }
}
