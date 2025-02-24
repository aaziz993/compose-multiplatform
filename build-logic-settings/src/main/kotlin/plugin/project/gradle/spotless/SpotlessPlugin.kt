package plugin.project.gradle.spotless

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.spotless
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.spotless
                .takeIf { it.enabled && projectProperties.kotlin.hasTargets }?.let { spotless ->
                    plugins.apply(settings.libs.plugins.plugin("spotless").id)

                    spotless.applyTo()
                }
        }
    }
}
