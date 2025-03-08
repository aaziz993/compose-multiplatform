package plugin.project.cmp.model

import gradle.id
import gradle.libs
import gradle.model.cmp.android.model.AndroidExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.cmp.desktop.model.DesktopExtension
import gradle.model.cmp.resources.model.ResourcesExtension
import gradle.model.project.EnabledSettings

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
