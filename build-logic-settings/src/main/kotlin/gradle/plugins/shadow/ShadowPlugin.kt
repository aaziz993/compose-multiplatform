package gradle.plugins.shadow

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.shadow.model.ShadowSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShadowPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.shadow?.takeIf{ pluginManager.hasPlugin("shadow") }?.let { shadow ->
                    plugins.apply(project.settings.libs.plugin("shadow").id)

                    shadow.applyTo()
                }
        }
    }
}
