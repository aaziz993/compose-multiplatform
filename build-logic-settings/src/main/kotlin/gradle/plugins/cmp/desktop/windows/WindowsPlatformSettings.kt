package gradle.plugins.cmp.desktop.windows

import gradle.api.trySet
import gradle.plugins.cmp.desktop.AbstractPlatformSettings
import gradle.plugins.cmp.desktop.FileAssociation
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.WindowsPlatformSettings

@Serializable
internal data class WindowsPlatformSettings(
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: Set<FileAssociation>? = null,
    var console: Boolean? = null,
    var dirChooser: Boolean? = null,
    var perUserInstall: Boolean? = null,
    var shortcut: Boolean? = null,
    var menu: Boolean? = null,
    var menuGroup: String? = null,
    var upgradeUuid: String? = null,
    var msiPackageVersion: String? = null,
    var exePackageVersion: String? = null,
) : AbstractPlatformSettings<WindowsPlatformSettings>() {

    context(Project)
    override fun applyTo(recipient: WindowsPlatformSettings) {
        super.applyTo(recipient)

        recipient::console trySet console
        recipient::dirChooser trySet dirChooser
        recipient::perUserInstall trySet perUserInstall
        recipient::shortcut trySet shortcut
        recipient::menu trySet menu
        recipient::menuGroup trySet menuGroup
        recipient::upgradeUuid trySet upgradeUuid
        recipient::msiPackageVersion trySet msiPackageVersion
        recipient::exePackageVersion trySet exePackageVersion
    }
}
