package plugin.project.gradle.knit

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import kotlinx.knit.KnitPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.knit
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { knit ->
                    plugins.apply(settings.libs.plugins.plugin("knit").id)

                    knit.applyTo()
                }
        }
    }
}
