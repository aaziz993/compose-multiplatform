package gradle.plugins.spotless

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.spotless.model.SpotlessSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.spotless?.takeIf{ pluginManager.hasPlugin("spotless") }?.let { spotless ->
                    plugins.apply(project.settings.libs.plugin("spotless").id)

                    spotless.applyTo()
                }
        }
    }
}
