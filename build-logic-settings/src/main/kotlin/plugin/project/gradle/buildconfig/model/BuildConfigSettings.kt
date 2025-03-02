package plugin.project.gradle.buildconfig.model

import gradle.buildConfig
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

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
