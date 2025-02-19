package plugin.project.kotlin.allopen

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class AllOpenPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.kotlin.allOpen.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.allopen.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureAllOpenExtension()
        }
    }
}
