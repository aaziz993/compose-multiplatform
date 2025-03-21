package gradle.plugins.develocity

import com.gradle.develocity.agent.gradle.scan.BuildScanPublishingConfiguration
import gradle.api.isCI
import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanPublishingConfiguration(
    val ifAuthenticated: Boolean? = null
) {

    fun applyTo(recipient: BuildScanPublishingConfiguration) {
        recipient.onlyIf { ifAuthenticated != false && isCI }
    }
}
