package gradle.plugins.kotlin.benchmark

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import gradle.plugins.kotlin.benchmark.model.BenchmarkSettings

internal class BenchmarkPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.benchmark
                .takeIf(BenchmarkSettings::enabled)?.let { benchmark ->
                    plugins.apply(project.settings.libs.plugins.plugin("kotlinx.benchmark").id)

                    benchmark.applyTo()
                }
        }
    }
}
