package gradle.plugins.kotlin.benchmark

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BenchmarkPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply kotlin benchmark properties.
            projectProperties.benchmark?.applyTo()
        }
    }
}
