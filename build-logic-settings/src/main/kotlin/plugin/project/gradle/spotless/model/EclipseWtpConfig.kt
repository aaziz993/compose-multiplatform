package plugin.project.gradle.spotless.model

import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import kotlinx.serialization.Serializable

@Serializable
internal data class EclipseWtpConfig(
    val version: String? = null,
    val type: EclipseWtpFormatterStep,
    val settingsFiles: List<String>? = null,
)
