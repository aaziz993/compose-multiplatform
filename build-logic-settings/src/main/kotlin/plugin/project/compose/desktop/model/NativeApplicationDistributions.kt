package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.NativeApplicationDistributions
import plugin.project.compose.desktop.model.macos.NativeApplicationMacOSPlatformSettings

@Serializable
internal data class NativeApplicationDistributions(
    override val outputBaseDir: String? = null,
    override val packageName: String? = null,
    override val packageVersion: String? = null,
    override val copyright: String? = null,
    override val description: String? = null,
    override val vendor: String? = null,
    override val appResourcesRootDir: String? = null,
    override val licenseFile: String? = null,
    val macOS: NativeApplicationMacOSPlatformSettings? = null,
) : AbstractDistributions() {

    context(Project)
    fun applyTo(distributions: NativeApplicationDistributions) {
        super.applyTo(distributions)
        macOS?.applyTo(distributions.macOS)
    }
}
