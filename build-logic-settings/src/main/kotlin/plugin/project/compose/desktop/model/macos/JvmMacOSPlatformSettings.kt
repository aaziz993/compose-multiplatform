package plugin.project.compose.desktop.model.macos

import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmMacOSPlatformSettings
import plugin.project.compose.desktop.model.FileAssociation

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
    override val fileAssociations: List<FileAssociation>? = null,
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

    ) : AbstractMacOSPlatformSettings() {

    context(Project)
    fun applyTo(settings: JvmMacOSPlatformSettings) {
        super.applyTo(settings)

        settings::dockName trySet dockName
        settings::setDockNameSameAsPackageName trySet setDockNameSameAsPackageName
        settings::appStore trySet appStore
        settings.entitlementsFile tryAssign entitlementsFile?.let(::file)
        settings.runtimeEntitlementsFile tryAssign runtimeEntitlementsFile?.let(::file)
        settings::pkgPackageVersion trySet pkgPackageVersion
        settings::pkgPackageBuildVersion trySet pkgPackageBuildVersion
        settings.provisioningProfile tryAssign provisioningProfile?.let(::file)
        settings.runtimeProvisioningProfile tryAssign runtimeProvisioningProfile?.let(::file)
        infoPlist?.let { infoPlist ->
            settings.infoPlist(infoPlist::applyTo)
        }
    }
}
