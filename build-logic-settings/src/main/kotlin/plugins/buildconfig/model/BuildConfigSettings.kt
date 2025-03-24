package plugins.buildconfig.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigSettings(
    override val sourceSets: List<String>? = null,
    override val enabled: Boolean = true,
) : BuildConfigExtension, EnabledSettings {

    context(project: Project)
    override fun applyTo() = project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("build.config").id) {
        super.applyTo()
    }
}
