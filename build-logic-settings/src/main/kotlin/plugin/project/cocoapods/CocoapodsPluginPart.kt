package plugin.project.cocoapods

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project
import plugin.project.kotlin.allopen.configureAllOpenExtension

internal class CocoapodsPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.cocoapods.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.cocoapods.get().pluginId)

        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureCocoapodsExtension()
        }
    }
}
