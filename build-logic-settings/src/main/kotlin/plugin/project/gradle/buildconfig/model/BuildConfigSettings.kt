package plugin.project.gradle.buildconfig.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.plugins.project.EnabledSettings
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigSettings(
    override val sourceSets: List<String>? = null,
    override val enabled: Boolean = true,
) : BuildConfigExtension, EnabledSettings {

    context(Project)
    override fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("build.config").id) {
        super.applyTo()
    }
}
