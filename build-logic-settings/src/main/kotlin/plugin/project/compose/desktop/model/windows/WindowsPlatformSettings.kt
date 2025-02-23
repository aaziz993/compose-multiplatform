package plugin.project.compose.desktop.model.windows

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.WindowsPlatformSettings
import plugin.project.compose.desktop.model.AbstractPlatformSettings
import plugin.project.compose.desktop.model.FileAssociation

@Serializable
internal data class WindowsPlatformSettings(
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: List<FileAssociation>? = null,
    var console: Boolean? = null,
    var dirChooser: Boolean? = null,
    var perUserInstall: Boolean? = null,
    var shortcut: Boolean? = null,
    var menu: Boolean? = null,
    var menuGroup: String?? = null,
    var upgradeUuid: String?? = null,
    var msiPackageVersion: String?? = null,
    var exePackageVersion: String?? = null,
) : AbstractPlatformSettings() {

    context(Project)
    fun applyTo(settings: WindowsPlatformSettings) {
        super.applyTo(settings)

        settings::console trySet console
        settings::dirChooser trySet dirChooser
        settings::perUserInstall trySet perUserInstall
        settings::shortcut trySet shortcut
        settings::menu trySet menu
        settings::menuGroup trySet menuGroup
        settings::upgradeUuid trySet upgradeUuid
        settings::msiPackageVersion trySet msiPackageVersion
        settings::exePackageVersion trySet exePackageVersion
    }
}
