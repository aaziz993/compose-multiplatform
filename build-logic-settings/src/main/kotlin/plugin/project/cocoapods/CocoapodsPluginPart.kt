package plugin.project.cocoapods

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import plugin.project.BindingPluginPart
import plugin.project.model.target.TargetType
import plugin.project.model.target.contains

internal class CocoapodsPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        if (TargetType.APPLE !in project.moduleProperties.targets) {
            project.logger.warn(
                "Unnecessary to enable cocoapods plugin when no apple targets represented. " +
                    "Module: ${project.name}",
            )
            return@lazy false
        }
        project.moduleProperties.settings.cocoapods.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
//            plugins.apply(project.libs.plugins.cocoapods.get().pluginId)

//        applySettings()
    }

    private fun applySettings() {
        with(project) {
            configureCocoapodsExtension()
        }
    }
}
