package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class EclipseConfig(
    val formatterVersion: String? = null,
    val settingsFiles: List<String>? = null,
    val p2Mirrors: Map<String, String>? = null
) {

    fun applyTo(eclipse: JavaExtension.EclipseConfig) {
        settingsFiles?.let { eclipse.configFile(*it.toTypedArray()) }
        p2Mirrors?.let(eclipse::withP2Mirrors)
    }
}

