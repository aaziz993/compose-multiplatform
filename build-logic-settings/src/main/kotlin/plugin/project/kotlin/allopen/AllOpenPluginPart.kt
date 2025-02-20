package plugin.project.kotlin.allopen

import gradle.moduleProperties
import gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AllOpenPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.allOpen.enabled || moduleProperties.targets == null) {
                return@with
            }

            plugins.apply(project.libs.plugins.allopen.get().pluginId)

            configureAllOpenExtension()
        }
    }
}
