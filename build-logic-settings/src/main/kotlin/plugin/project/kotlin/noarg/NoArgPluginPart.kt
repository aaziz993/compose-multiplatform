package plugin.project.kotlin.noarg

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.noArg.enabled || moduleProperties.targets.isEmpty())

                plugins.apply(project.libs.plugins.allopen.get().pluginId)

            configureNoArgExtension()
        }
    }
}
