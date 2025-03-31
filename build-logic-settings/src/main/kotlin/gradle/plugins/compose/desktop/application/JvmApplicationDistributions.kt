package gradle.plugins.compose.desktop.application

import gradle.collection.tryAddAll
import gradle.collection.trySet
import gradle.plugins.compose.desktop.AbstractDistributions
import gradle.plugins.compose.desktop.linux.LinuxPlatformSettings
import gradle.plugins.compose.desktop.macos.JvmMacOSPlatformSettings
import gradle.plugins.compose.desktop.windows.WindowsPlatformSettings
import gradle.reflect.trySet
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

        receiver.modules tryAddAll modules
        receiver.modules trySet setModules
        receiver::includeAllModules trySet includeAllModules
        linux?.applyTo(receiver.linux)
        macOS?.applyTo(receiver.macOS)
        windows?.applyTo(receiver.windows)
    }
}
