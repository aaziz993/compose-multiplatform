package plugin.project.cmp.model

import gradle.id
import gradle.libs
import gradle.plugins.cmp.android.model.AndroidExtension
import gradle.plugins.cmp.desktop.model.DesktopExtension
import gradle.plugins.cmp.resources.model.ResourcesExtension
import gradle.plugins.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
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
        pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
            desktop.applyTo()
            android.applyTo()
            resources.applyTo()
        }
}
