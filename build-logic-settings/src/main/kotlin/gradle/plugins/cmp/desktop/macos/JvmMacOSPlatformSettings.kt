package gradle.plugins.cmp.desktop.macos

import gradle.api.tryAssign
import gradle.api.trySet
import gradle.plugins.cmp.desktop.FileAssociation
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
    var dockName: String? = null,
    var setDockNameSameAsPackageName: Boolean? = null,
    var appStore: Boolean? = null,
    var entitlementsFile: String? = null,
    var runtimeEntitlementsFile: String? = null,
    var pkgPackageVersion: String? = null,
    var pkgPackageBuildVersion: String? = null,
    val provisioningProfile: String? = null,
    val runtimeProvisioningProfile: String? = null,
    val infoPlist: InfoPlistSettings? = null,
) : AbstractMacOSPlatformSettings<JvmMacOSPlatformSettings>() {

    context(Project)
    override fun applyTo(recipient: JvmMacOSPlatformSettings) {
        super.applyTo(recipient)

        recipient::dockName trySet dockName
        recipient::setDockNameSameAsPackageName trySet setDockNameSameAsPackageName
        recipient::appStore trySet appStore
        recipient.entitlementsFile tryAssign entitlementsFile?.let(::file)
        recipient.runtimeEntitlementsFile tryAssign runtimeEntitlementsFile?.let(::file)
        recipient::pkgPackageVersion trySet pkgPackageVersion
        recipient::pkgPackageBuildVersion trySet pkgPackageBuildVersion
        recipient.provisioningProfile tryAssign provisioningProfile?.let(::file)
        recipient.runtimeProvisioningProfile tryAssign runtimeProvisioningProfile?.let(::file)
        infoPlist?.let { infoPlist ->
            recipient.infoPlist(infoPlist::applyTo)
        }
    }
}
