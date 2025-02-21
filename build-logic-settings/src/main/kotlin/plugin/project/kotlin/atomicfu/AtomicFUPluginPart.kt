package plugin.project.kotlin.atomicfu

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AtomicFUPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!moduleProperties.settings.kotlin.atomicFU.enabled || moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.atomicfu.get().pluginId)

            configureAtomicFUPluginExtension()
        }
    }
}
