package gradle.plugins.develocity.buildscan

import com.gradle.develocity.agent.gradle.scan.BuildScanPublishingConfiguration

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanPublishingConfiguration(
    val ifAuthenticated: Boolean? = null
) {

    fun applyTo(receiver: BuildScanPublishingConfiguration) {
        receiver.onlyIf { ifAuthenticated != false && isCI }
    }
}
