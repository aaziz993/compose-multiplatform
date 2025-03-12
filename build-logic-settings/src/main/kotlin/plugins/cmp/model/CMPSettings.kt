package plugins.cmp.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.cmp.android.model.AndroidExtension
import gradle.plugins.cmp.desktop.model.DesktopExtension
import gradle.plugins.cmp.resources.model.ResourcesExtension
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
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
