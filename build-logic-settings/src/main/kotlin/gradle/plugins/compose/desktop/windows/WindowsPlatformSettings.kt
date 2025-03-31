package gradle.plugins.compose.desktop.windows

import gradle.plugins.compose.desktop.platform.AbstractPlatformSettings
import gradle.plugins.compose.desktop.platform.FileAssociation
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.WindowsPlatformSettings

@Serializable
internal data class WindowsPlatformSettings(
    override val iconFile: String? = null,
    override val packageVersion: String? = null,
    override val installationPath: String? = null,
    override val fileAssociations: Set<FileAssociation>? = null,
    val console: Boolean? = null,
    val dirChooser: Boolean? = null,
    val perUserInstall: Boolean? = null,
    val shortcut: Boolean? = null,
    val menu: Boolean? = null,
    val menuGroup: String? = null,
    val upgradeUuid: String? = null,
    val msiPackageVersion: String? = null,
    val exePackageVersion: String? = null,
) : AbstractPlatformSettings<WindowsPlatformSettings>() {

    context(Project)
    override fun applyTo(receiver: WindowsPlatformSettings) {
        super.applyTo(receiver)

        receiver::console trySet console
        receiver::dirChooser trySet dirChooser
        receiver::perUserInstall trySet perUserInstall
        receiver::shortcut trySet shortcut
        receiver::menu trySet menu
        receiver::menuGroup trySet menuGroup
        receiver::upgradeUuid trySet upgradeUuid
        receiver::msiPackageVersion trySet msiPackageVersion
        receiver::exePackageVersion trySet exePackageVersion
    }
}
