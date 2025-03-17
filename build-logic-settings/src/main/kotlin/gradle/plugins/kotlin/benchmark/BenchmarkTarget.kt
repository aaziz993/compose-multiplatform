package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal abstract class BenchmarkTarget {

    abstract val name: String
    abstract val workingDir: String?

    context(Project)
    open fun applyTo(target: kotlinx.benchmark.gradle.BenchmarkTarget) {
        target::workingDir trySet workingDir
    }
}

internal object BenchmarkTargetTransformingSerializer : KeyTransformingSerializer<BenchmarkTarget>(
    BenchmarkTarget.serializer(),
    "type",
)
