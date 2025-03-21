package gradle.plugins.cmp.desktop

import gradle.plugins.cmp.desktop.macos.NativeApplicationMacOSPlatformSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.NativeApplicationDistributions

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
) : AbstractDistributions<NativeApplicationDistributions>() {

    context(Project)
    override fun applyTo(recipient: NativeApplicationDistributions) {
        super.applyTo(recipient)
        macOS?.applyTo(recipient.macOS)
    }
}
