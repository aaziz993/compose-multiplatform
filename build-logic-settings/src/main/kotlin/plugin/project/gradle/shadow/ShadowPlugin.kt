package plugin.project.gradle.shadow

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShadowPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.shadow
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { shadow ->
                    plugins.apply(settings.libs.plugins.plugin("shadow").id)

                    shadow.applyTo()
                }
        }
    }
}
