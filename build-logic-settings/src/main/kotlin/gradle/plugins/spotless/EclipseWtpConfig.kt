package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import kotlinx.serialization.Serializable

@Serializable
internal data class EclipseWtpConfig(
    val version: String? = null,
    val type: EclipseWtpFormatterStep,
    val settingsFiles: List<String>? = null,
) {

    fun applyTo(eclipse: FormatExtension.EclipseWtpConfig) {
        settingsFiles?.let { eclipse.configFile(*it.toTypedArray()) }
    }
}
