package plugin.project.gradle.buildconfig.model

import gradle.id
import gradle.libs
import gradle.model.buildconfig.BuildConfigExtension
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.EnabledSettings

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
