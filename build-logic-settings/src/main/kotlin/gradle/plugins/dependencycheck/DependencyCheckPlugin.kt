package gradle.plugins.dependencycheck

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.dependencycheck.model.DependencyCheckSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DependencyCheckPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.dependencyCheck?.takeIf{ pluginManager.hasPlugin("dependencyCheck") }?.let { dependencyCheck ->
                    plugins.apply(project.settings.libs.plugin("dependencycheck").id)

                    dependencyCheck.applyTo()
                }
        }
    }
}
