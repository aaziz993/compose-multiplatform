package plugins.spotless

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.spotless.model.SpotlessSettings

internal class SpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.spotless
                .takeIf (SpotlessSettings::enabled)?.let { spotless ->
                    plugins.apply(project.settings.libs.plugins.plugin("spotless").id)

                    spotless.applyTo()
                }
        }
    }
}
