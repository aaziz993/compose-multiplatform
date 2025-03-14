package gradle.plugins.cmp.desktop.linux

import gradle.api.trySet
import gradle.plugins.cmp.desktop.AbstractPlatformSettings
import gradle.plugins.cmp.desktop.FileAssociation
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.LinuxPlatformSettings

@Serializable
internal data class LinuxPlatformSettings(
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: List<FileAssociation>? = null,
    val shortcut: Boolean? = null,
    val packageName: String? = null,
    val appRelease: String? = null,
    val appCategory: String? = null,
    val debMaintainer: String? = null,
    val menuGroup: String? = null,
    val rpmLicenseType: String? = null,
    val debPackageVersion: String? = null,
    val rpmPackageVersion: String? = null,
) : AbstractPlatformSettings() {

    context(Project)
    fun applyTo(settings: LinuxPlatformSettings) {
        super.applyTo(settings)

        settings::shortcut trySet shortcut
        settings::packageName trySet packageName
        settings::appRelease trySet appRelease
        settings::appCategory trySet appCategory
        settings::debMaintainer trySet debMaintainer
        settings::menuGroup trySet menuGroup
        settings::rpmLicenseType trySet rpmLicenseType
        settings::debPackageVersion trySet debPackageVersion
        settings::rpmPackageVersion trySet rpmPackageVersion
    }
}
