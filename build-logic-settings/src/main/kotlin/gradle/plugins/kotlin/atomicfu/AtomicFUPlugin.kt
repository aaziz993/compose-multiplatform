package gradle.plugins.kotlin.atomicfu

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import gradle.plugins.kotlin.atomicfu.model.AtomicFUSettings

internal class AtomicFUPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.atomicFU
                .takeIf(AtomicFUSettings::enabled)?.let { atomicFU ->
                    plugins.apply(project.settings.libs.plugins.plugin("atomicfu").id)

                    atomicFU.applyTo()
                }
        }
    }
}
