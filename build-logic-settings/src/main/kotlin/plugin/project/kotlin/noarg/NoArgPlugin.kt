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
            projectProperties.plugins.noArg
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { noArg ->
                    plugins.apply(settings.libs.plugins.plugin("allopen").id)

                    noArg.applyTo()
                }
        }
    }
}
