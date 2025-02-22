package plugin.project.kotlin.atomicfu

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AtomicFUPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.plugins.atomicFU.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.atomicfu.get().pluginId)

            configureAtomicFUPluginExtension()
        }
    }
}
