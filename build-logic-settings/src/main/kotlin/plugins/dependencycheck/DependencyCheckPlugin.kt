package plugins.dependencycheck

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.dependencycheck.model.DependencyCheckSettings

internal class DependencyCheckPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.dependencyCheck
                .takeIf(DependencyCheckSettings::enabled)?.let { dependencyCheck ->
                    plugins.apply(project.settings.libs.plugins.plugin("dependencycheck").id)

                    dependencyCheck.applyTo()
                }
        }
    }
}
