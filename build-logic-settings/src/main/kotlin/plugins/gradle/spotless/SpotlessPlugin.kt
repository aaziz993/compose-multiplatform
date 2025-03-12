package plugins.gradle.spotless

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SpotlessPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.spotless
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { spotless ->
                    plugins.apply(settings.libs.plugins.plugin("spotless").id)

                    spotless.applyTo()
                }
        }
    }
}
