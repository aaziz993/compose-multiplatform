package gradle.plugins.kotlin.benchmark

import gradle.accessors.benchmark
import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BenchmarksExtension {

    abstract val benchsDescriptionDir: String?

    abstract val buildDir: String?

    abstract val configurations: LinkedHashSet<BenchmarkConfiguration>?

    abstract val kotlinCompilerVersion: String?

    abstract val reportsDir: String?

    abstract val targets: LinkedHashSet<@Serializable(with = BenchmarkTargetKeyTransformingSerializer::class) BenchmarkTarget<out kotlinx.benchmark.gradle.BenchmarkTarget>>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("kotlinx.benchmark").id) {
            project.benchmark::benchsDescriptionDir trySet benchsDescriptionDir
            project.benchmark::buildDir trySet buildDir

            configurations?.forEach { configuration ->
                configuration.applyTo(project.benchmark.configurations)
            }

            project.benchmark.kotlinCompilerVersion tryAssign (kotlinCompilerVersion
                ?: project.settings.libs.version("kotlin"))
            project.benchmark::reportsDir trySet reportsDir

            targets?.forEach { target ->
                (target as BenchmarkTarget<kotlinx.benchmark.gradle.BenchmarkTarget>).applyTo(project.benchmark.targets)
            }
        }
}
