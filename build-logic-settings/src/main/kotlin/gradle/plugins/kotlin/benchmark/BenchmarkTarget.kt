package gradle.plugins.kotlin.benchmark

import gradle.api.BaseNamed

import gradle.api.trySet
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = BenchmarkTargetSerializer::class)
internal abstract class BenchmarkTarget : BaseNamed<kotlinx.benchmark.gradle.BenchmarkTarget> {

    abstract val workingDir: String?

        context(Project)
    override fun applyTo(named: T) {
        TODO("Not yet implemented")
    }

    context(Project)
    open fun applyTo(target: kotlinx.benchmark.gradle.BenchmarkTarget) {
        target::workingDir trySet workingDir
    }
}

private object BenchmarkTargetSerializer : JsonPolymorphicSerializer<BenchmarkTarget>(
    BenchmarkTarget::class,
)

internal object BenchmarkTargetTransformingSerializer : KeyTransformingSerializer<BenchmarkTarget>(
    BenchmarkTarget.serializer(),
    "type",
)
