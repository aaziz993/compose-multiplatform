package gradle.plugins.kotlin.benchmark

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.benchmark.model.BenchmarkSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BenchmarkPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.benchmark
                .takeIf(BenchmarkSettings::enabled)?.let { benchmark ->
                    plugins.apply(project.settings.libs.plugin("kotlinx.benchmark").id)

                    benchmark.applyTo()
                }
        }
    }
}
