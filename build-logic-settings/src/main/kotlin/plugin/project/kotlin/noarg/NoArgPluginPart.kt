package plugin.project.kotlin.noarg

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.noArg.enabled || projectProperties.kotlin.hasTargets)

                plugins.apply(project.libs.plugins.allopen.get().pluginId)

            configureNoArgExtension()
        }
    }
}
