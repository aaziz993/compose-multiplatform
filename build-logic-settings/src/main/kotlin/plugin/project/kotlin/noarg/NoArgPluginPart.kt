package plugin.project.kotlin.noarg

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.noArg.enabled || projectProperties.kotlin.hasTargets)

                plugins.apply(settings.libs.plugins.plugin("allopen").id)

            configureNoArgExtension()
        }
    }
}
