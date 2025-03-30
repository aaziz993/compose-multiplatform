package gradle.plugins.shadow.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.api.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ShadowSettings(
    override val enabled: Boolean = true
) : EnabledSettings {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("kotlin.serialization").id) {

        }
}
