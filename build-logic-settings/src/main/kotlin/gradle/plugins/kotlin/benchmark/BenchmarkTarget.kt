package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import kotlinx.benchmark.gradle.BenchmarkTarget
import kotlinx.serialization.Serializable

@Serializable
internal data class BenchmarkTarget(
    val name: String = "",
    val workingDir: String? = null,
) {

    fun applyTo(target: BenchmarkTarget) {
        target::workingDir trySet workingDir
    }
}
