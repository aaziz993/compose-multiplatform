package gradle.plugins.compose.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.compose.android.AndroidExtension
import gradle.plugins.compose.desktop.DesktopExtension
import gradle.plugins.compose.resources.ResourcesExtension
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class CMPSettings(
    val desktop: DesktopExtension = DesktopExtension(),
    val android: AndroidExtension = AndroidExtension(),
    val resources: ResourcesExtension = ResourcesExtension(),
    override val enabled: Boolean = false
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("compose.multiplatform").id) {
            desktop.applyTo()
            android.applyTo()
            resources.applyTo()
        }
}
