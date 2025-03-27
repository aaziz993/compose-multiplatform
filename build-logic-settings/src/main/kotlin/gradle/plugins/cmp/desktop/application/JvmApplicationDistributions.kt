package gradle.plugins.cmp.desktop.application

import gradle.act
import gradle.api.trySet
import gradle.plugins.cmp.desktop.AbstractDistributions
import gradle.plugins.cmp.desktop.linux.LinuxPlatformSettings
import gradle.plugins.cmp.desktop.macos.JvmMacOSPlatformSettings
import gradle.plugins.cmp.desktop.windows.WindowsPlatformSettings
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
    val modules: Set<String>? = null,
    val setModules: Set<String>? = null,
    val includeAllModules: Boolean? = null,
    val linux: LinuxPlatformSettings? = null,
    val macOS: JvmMacOSPlatformSettings? = null,
    val windows: WindowsPlatformSettings? = null,
) : AbstractDistributions<JvmApplicationDistributions>() {

    context(Project)
    override fun applyTo(receiver: JvmApplicationDistributions) {
        super.applyTo(receiver)

        modules?.let(receiver.modules::addAll)
        setModules?.act(receiver.modules::clear)?.let(receiver.modules::addAll)
        receiver::includeAllModules trySet includeAllModules
        linux?.applyTo(receiver.linux)
        macOS?.applyTo(receiver.macOS)
        windows?.applyTo(receiver.windows)
    }
}
