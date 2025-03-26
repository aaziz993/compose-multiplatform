package gradle.plugins.buildconfig

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.buildconfig.model.BuildConfigSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.buildConfig
                .takeIf(BuildConfigSettings::enabled)?.let { buildConfig ->
                    plugins.apply(project.settings.libs.plugins.plugin("buildConfig").id)

                    buildConfig.applyTo()
                }
        }
    }
}
