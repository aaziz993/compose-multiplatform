package plugin.project.gradle.kover

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoverPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.plugins.kover.enabled ||projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.kover.get().pluginId)

            configureKoverExtension()
        }
    }
}
