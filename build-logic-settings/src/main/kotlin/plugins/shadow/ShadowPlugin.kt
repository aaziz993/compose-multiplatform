package plugins.shadow

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShadowPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.shadow
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { shadow ->
                    plugins.apply(settings.libs.plugins.plugin("shadow").id)

                    shadow.applyTo()
                }
        }
    }
}
