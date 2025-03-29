package gradle.plugins.kotlin.benchmark.model

import gradle.plugins.kotlin.benchmark.BenchmarkConfiguration
import gradle.plugins.kotlin.benchmark.BenchmarkTarget
import gradle.plugins.kotlin.benchmark.BenchmarksExtension
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
internal data class BenchmarkSettings(
    override var benchsDescriptionDir: String? = null,
    override var buildDir: String? = null,
    override val configurations: LinkedHashSet<BenchmarkConfiguration>? = null,
    override val kotlinCompilerVersion: String? = null,
    override var reportsDir: String? = null,
    override val targets: LinkedHashSet<BenchmarkTarget<out @Contextual kotlinx.benchmark.gradle.BenchmarkTarget>>? = null,
    override val enabled: Boolean = true
) : BenchmarksExtension(), EnabledSettings
