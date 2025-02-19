package plugin.project.gradle.kover

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class KoverPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.gradle.kover.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.kover.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureKoverExtension()
        }
    }
}
