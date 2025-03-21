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
    fun applyTo(recipient: ProguardSettings) {
        recipient.version tryAssign version
        recipient.maxHeapSize tryAssign maxHeapSize
        configurationFiles?.toTypedArray()?.let(recipient.configurationFiles::from)
        setConfigurationFiles?.let(recipient.configurationFiles::setFrom)
        recipient.isEnabled tryAssign isEnabled
        recipient.obfuscate tryAssign obfuscate
        recipient.optimize tryAssign optimize
        recipient.joinOutputJars tryAssign joinOutputJars
    }
}
