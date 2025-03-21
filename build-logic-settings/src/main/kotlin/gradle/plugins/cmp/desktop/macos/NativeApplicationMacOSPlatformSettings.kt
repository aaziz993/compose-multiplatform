package gradle.plugins.cmp.desktop.macos

import gradle.plugins.cmp.desktop.platform.FileAssociation
import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.application.dsl.NativeApplicationMacOSPlatformSettings

@Serializable
internal data class NativeApplicationMacOSPlatformSettings(
    override val packageName: String? = null,
    override val packageBuildVersion: String? = null,
    override val dmgPackageVersion: String? = null,
    override val dmgPackageBuildVersion: String? = null,
    override val appCategory: String? = null,
    override val minimumSystemVersion: String? = null,
    override val bundleID: String? = null,
    override val signing: MacOSSigningSettings? = null,
    override val notarization: MacOSNotarizationSettings? = null,
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: Set<FileAssociation>? = null,
) : AbstractMacOSPlatformSettings<NativeApplicationMacOSPlatformSettings>()
