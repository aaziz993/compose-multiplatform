package gradle.plugins.kotlin.atomicfu

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AtomicFUPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply atomicFu properties
            projectProperties.atomicfu?.applyTo()
        }
    }
}
