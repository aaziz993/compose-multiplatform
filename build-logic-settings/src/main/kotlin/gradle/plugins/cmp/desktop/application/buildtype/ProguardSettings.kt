package gradle.plugins.cmp.desktop.application.buildtype

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.ProguardSettings

@Serializable
internal data class ProguardSettings(
    val version: String? = null,
    val maxHeapSize: String? = null,
    val configurationFiles: LinkedHashSet<String>? = null,
    val setConfigurationFiles: LinkedHashSet<String>? = null,
    val isEnabled: Boolean? = null,
    val obfuscate: Boolean? = null,
    val optimize: Boolean? = null,
    val joinOutputJars: Boolean? = null,
) {

    context(Project)
    fun applyTo(receiver: ProguardSettings) {
        receiver.version tryAssign version
        receiver.maxHeapSize tryAssign maxHeapSize
        receiver.configurationFiles tryFrom configurationFiles
        receiver.configurationFiles trySetFrom setConfigurationFiles
        receiver.isEnabled tryAssign isEnabled
        receiver.obfuscate tryAssign obfuscate
        receiver.optimize tryAssign optimize
        receiver.joinOutputJars tryAssign joinOutputJars
    }
}
