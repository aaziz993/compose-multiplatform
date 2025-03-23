package gradle.plugins.kotlin.benchmark

import gradle.accessors.benchmark
import gradle.accessors.id
import gradle.accessors.libs
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

    abstract val configurations: Set<BenchmarkConfiguration>?

    abstract val kotlinCompilerVersion: String?

    abstract val reportsDir: String?

    abstract val targets: List<@Serializable(with = BenchmarkTargetTransformingSerializer::class) BenchmarkTarget>?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlinx.benchmark").id) {
            benchmark::benchsDescriptionDir trySet benchsDescriptionDir
            benchmark::buildDir trySet buildDir

            configurations?.forEach { configuration ->
                configuration.applyTo(benchmark.configurations)
            }

            benchmark.kotlinCompilerVersion tryAssign (kotlinCompilerVersion
                ?: settings.libs.versions.version("kotlin"))
            benchmark::reportsDir trySet reportsDir

            targets?.forEach { target ->
                target.applyTo(benchmark.targets)
            }
        }
}
