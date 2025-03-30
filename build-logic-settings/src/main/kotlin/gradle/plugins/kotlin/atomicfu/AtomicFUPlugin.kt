package gradle.plugins.kotlin.atomicfu

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.atomicfu.model.AtomicFUSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AtomicFUPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.atomicFU?.takeIf{ pluginManager.hasPlugin("atomicFU") }?.let { atomicFU ->
                    plugins.apply(project.settings.libs.plugin("atomicfu").id)

                    atomicFU.applyTo()
                }
        }
    }
}
