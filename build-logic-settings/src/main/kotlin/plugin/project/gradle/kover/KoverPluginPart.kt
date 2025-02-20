package plugin.project.gradle.kover

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoverPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.gradle.kover.enabled || moduleProperties.targets == null) {
                return@with
            }

            plugins.apply(project.libs.plugins.kover.get().pluginId)

            configureKoverExtension()
        }
    }
}
