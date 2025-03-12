package gradle.plugins.cmp.desktop.model

import gradle.plugins.cmp.desktop.model.linux.LinuxPlatformSettings
import gradle.plugins.cmp.desktop.model.macos.JvmMacOSPlatformSettings
import gradle.plugins.cmp.desktop.model.windows.WindowsPlatformSettings
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationDistributions

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
