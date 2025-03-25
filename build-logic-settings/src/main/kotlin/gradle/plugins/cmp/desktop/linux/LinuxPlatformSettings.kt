package gradle.plugins.cmp.desktop.linux

import gradle.api.trySet
import gradle.plugins.cmp.desktop.platform.AbstractPlatformSettings
import gradle.plugins.cmp.desktop.platform.FileAssociation
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.LinuxPlatformSettings

@Serializable
internal data class LinuxPlatformSettings(
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: Set<FileAssociation>? = null,
    val shortcut: Boolean? = null,
    val packageName: String? = null,
    val appRelease: String? = null,
    val appCategory: String? = null,
    val debMaintainer: String? = null,
    val menuGroup: String? = null,
    val rpmLicenseType: String? = null,
    val debPackageVersion: String? = null,
    val rpmPackageVersion: String? = null,
) : AbstractPlatformSettings<LinuxPlatformSettings>() {

    context(Project)
    override fun applyTo(receiver: LinuxPlatformSettings) {
        super.applyTo(receiver)

        receiver::shortcut trySet shortcut
        receiver::packageName trySet packageName
        receiver::appRelease trySet appRelease
        receiver::appCategory trySet appCategory
        receiver::debMaintainer trySet debMaintainer
        receiver::menuGroup trySet menuGroup
        receiver::rpmLicenseType trySet rpmLicenseType
        receiver::debPackageVersion trySet debPackageVersion
        receiver::rpmPackageVersion trySet rpmPackageVersion
    }
}
