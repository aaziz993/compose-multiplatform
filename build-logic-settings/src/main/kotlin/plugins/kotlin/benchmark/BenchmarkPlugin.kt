package plugins.kotlin.benchmark

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BenchmarkPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.benchmark
                .takeIf (::enabled)?.let { benchmark ->
                    plugins.apply(project.settings.libs.plugins.plugin("kotlinx.benchmark").id)

                    benchmark.applyTo()
                }
        }
    }
}
