package plugin.project.compose.model

import gradle.compose
import gradle.desktop
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.resources
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.compose.android.model.AndroidExtension
import plugin.project.compose.desktop.model.DesktopExtension
import plugin.project.compose.resources.model.ResourcesExtension
import plugin.project.model.EnabledSettings
import plugin.project.model.ProjectType

@Serializable
internal data class ComposeSettings(
    val desktop: DesktopExtension = DesktopExtension(),
    val android: AndroidExtension = AndroidExtension(),
    val resources: ResourcesExtension = ResourcesExtension(),
    override val enabled: Boolean = false
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
            desktop.applyTo()
            android.applyTo()
            resources.applyTo()
        }
}
