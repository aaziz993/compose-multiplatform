package gradle.plugins.compose.desktop.macos

import gradle.api.file.tryAssign
import gradle.plugins.compose.desktop.platform.FileAssociation
import klib.data.type.reflection.tryApply
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmMacOSPlatformSettings

@Serializable
internal data class JvmMacOSPlatformSettings(
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
    val dockName: String? = null,
    val setDockNameSameAsPackageName: Boolean? = null,
    val appStore: Boolean? = null,
    val entitlementsFile: String? = null,
    val runtimeEntitlementsFile: String? = null,
    val pkgPackageVersion: String? = null,
    val pkgPackageBuildVersion: String? = null,
    val provisioningProfile: String? = null,
    val runtimeProvisioningProfile: String? = null,
    val infoPlist: InfoPlistSettings? = null,
) : AbstractMacOSPlatformSettings<JvmMacOSPlatformSettings>() {

    context(Project)
    override fun applyTo(receiver: JvmMacOSPlatformSettings) {
        super.applyTo(receiver)

        receiver::dockName trySet dockName
        receiver::setDockNameSameAsPackageName trySet setDockNameSameAsPackageName
        receiver::appStore trySet appStore
        receiver.entitlementsFile tryAssign entitlementsFile?.let(project::file)
        receiver.runtimeEntitlementsFile tryAssign runtimeEntitlementsFile?.let(project::file)
        receiver::pkgPackageVersion trySet pkgPackageVersion
        receiver::pkgPackageBuildVersion trySet pkgPackageBuildVersion
        receiver.provisioningProfile tryAssign provisioningProfile?.let(project::file)
        receiver.runtimeProvisioningProfile tryAssign runtimeProvisioningProfile?.let(project::file)
        receiver::infoPlist tryApply infoPlist?.let { infoPlist -> infoPlist::applyTo }
    }
}
