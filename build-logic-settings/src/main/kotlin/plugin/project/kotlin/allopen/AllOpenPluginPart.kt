package plugin.project.kotlin.allopen

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AllOpenPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.allOpen.enabled ||projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.allopen.get().pluginId)

            configureAllOpenExtension()
        }
    }
}
