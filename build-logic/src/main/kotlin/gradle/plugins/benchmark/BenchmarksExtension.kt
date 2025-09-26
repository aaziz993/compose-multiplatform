package gradle.plugins.benchmark

import kotlinx.benchmark.gradle.BenchmarksExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.benchmark: BenchmarksExtension get() = the()

public fun Project.benchmark(configure: BenchmarksExtension.() -> Unit): Unit = extensions.configure(configure)
