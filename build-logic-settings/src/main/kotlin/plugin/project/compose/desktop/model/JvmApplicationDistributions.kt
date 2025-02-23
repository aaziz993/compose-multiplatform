package plugin.project.compose.desktop.model

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationDistributions
import plugin.project.compose.desktop.model.linux.LinuxPlatformSettings
import plugin.project.compose.desktop.model.macos.JvmMacOSPlatformSettings
import plugin.project.compose.desktop.model.windows.WindowsPlatformSettings

@Serializable
internal data class JvmApplicationDistributions(
    override val outputBaseDir: String? = null,
    override val packageName: String? = null,
    override val packageVersion: String? = null,
    override val copyright: String? = null,
    override val description: String? = null,
    override val vendor: String? = null,
    override val appResourcesRootDir: String? = null,
    override val licenseFile: String? = null,
    val modules: List<String>? = null,
    val includeAllModules: Boolean? = null,
    val linux: LinuxPlatformSettings? = null,
    val macOS: JvmMacOSPlatformSettings? = null,
    val windows: WindowsPlatformSettings? = null,
) : AbstractDistributions() {

    context(Project)
    fun applyTo(distributions: JvmApplicationDistributions) {
        super.applyTo(distributions)

        modules?.let(distributions.modules::addAll)
        distributions::includeAllModules trySet includeAllModules
        linux?.applyTo(distributions.linux)
        macOS?.applyTo(distributions.macOS)
        windows?.applyTo(distributions.windows)
    }
}
