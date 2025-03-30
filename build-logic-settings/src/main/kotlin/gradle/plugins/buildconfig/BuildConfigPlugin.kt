package gradle.plugins.buildconfig

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.buildconfig.model.BuildConfigSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.buildConfig?.takeIf{ pluginManager.hasPlugin("buildConfig") }?.let { buildConfig ->
                    plugins.apply(project.settings.libs.plugin("buildConfig").id)

                    buildConfig.applyTo()
                }
        }
    }
}
