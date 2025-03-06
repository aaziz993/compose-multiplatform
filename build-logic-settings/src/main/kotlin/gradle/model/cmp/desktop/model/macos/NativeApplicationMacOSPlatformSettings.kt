package gradle.model.cmp.desktop.model.macos

import kotlinx.serialization.Serializable
import gradle.model.cmp.desktop.model.FileAssociation

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
    override val fileAssociations: List<FileAssociation>? = null,
) : AbstractMacOSPlatformSettings()
