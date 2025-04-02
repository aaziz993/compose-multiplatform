package gradle.plugins.kotlin.benchmark

import gradle.accessors.benchmark
import gradle.api.catalog.libs
import gradle.accessors.settings
import gradle.api.applyTo
import gradle.api.provider.tryAssign
import klib.data.type.reflection.trySet
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BenchmarksExtension(
    val benchsDescriptionDir: String? = null,
    val buildDir: String? = null,
    val configurations: LinkedHashSet<BenchmarkConfiguration>? = null,
    val kotlinCompilerVersion: String? = null,
    val reportsDir: String? = null,
    val targets: LinkedHashSet<BenchmarkTarget<out @Contextual kotlinx.benchmark.gradle.BenchmarkTarget>>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlinx.benchmark") {
            project.benchmark::benchsDescriptionDir trySet benchsDescriptionDir
            project.benchmark::buildDir trySet buildDir

            configurations?.forEach { configuration ->
                configuration.applyTo(project.benchmark.configurations)
            }

            project.benchmark.kotlinCompilerVersion tryAssign (kotlinCompilerVersion
                ?: project.settings.libs.versionOrNull("kotlin"))
            project.benchmark::reportsDir trySet reportsDir

            targets?.forEach { target ->
                (target as BenchmarkTarget<kotlinx.benchmark.gradle.BenchmarkTarget>).applyTo(project.benchmark.targets)
            }
        }
}
