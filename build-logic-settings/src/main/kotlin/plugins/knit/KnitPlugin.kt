package plugins.knit

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.knit
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() == true }?.let { knit ->
                    plugins.apply(settings.libs.plugins.plugin("knit").id)

                    knit.applyTo()
                }
        }
    }
}
