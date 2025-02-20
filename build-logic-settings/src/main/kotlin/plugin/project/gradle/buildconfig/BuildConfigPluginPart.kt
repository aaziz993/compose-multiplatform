package plugin.project.gradle.buildconfig

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Project
import org.gradle.api.Plugin

internal class BuildConfigPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.gradle.buildConfig.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.build.config.get().pluginId)

            configureBuildConfigExtension()
        }
    }
}
