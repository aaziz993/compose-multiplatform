package plugin.project.compose.desktop.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.ProguardSettings

@Serializable
internal data class ProguardSettings(
    val version: String? = null,
    val maxHeapSize: String? = null,
    val configurationFiles: List<String>? = null,
    val isEnabled: Boolean? = null,
    val obfuscate: Boolean? = null,
    val optimize: Boolean? = null,
    val joinOutputJars: Boolean? = null,
) {

    context(Project)
    fun applyTo(settings: ProguardSettings) {
        settings.version tryAssign version
        settings.maxHeapSize tryAssign maxHeapSize
        configurationFiles?.let(settings.configurationFiles::setFrom)
        settings.isEnabled tryAssign isEnabled
        settings.obfuscate tryAssign obfuscate
        settings.optimize tryAssign optimize
        settings.joinOutputJars tryAssign joinOutputJars
    }
}
