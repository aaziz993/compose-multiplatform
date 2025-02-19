package plugin.project.gradle.buildconfig

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Project
import plugin.project.BindingPluginPart

internal class BuildConfigPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.gradle.buildConfig.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.build.config.get().pluginId)

        applySettings()
    }

    fun applySettings() {
        with(project) {
            configureBuildConfigExtension()
        }
    }
}
