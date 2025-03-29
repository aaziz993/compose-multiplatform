package gradle.plugins.kotlin.benchmark

import gradle.api.ProjectNamed
import gradle.api.trySet
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = BenchmarkTargetKeyTransformingSerializer::class)
internal abstract class BenchmarkTarget<T : kotlinx.benchmark.gradle.BenchmarkTarget> : ProjectNamed<T> {

    abstract val workingDir: String?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::workingDir trySet workingDir
    }
}

private object BenchmarkTargetContentPolymorphicSerialize : JsonContentPolymorphicSerializer<BenchmarkTarget<*>>(
    BenchmarkTarget::class,
)

private object BenchmarkTargetKeyTransformingSerializer : KeyTransformingSerializer<BenchmarkTarget<*>>(
    BenchmarkTargetContentPolymorphicSerialize,
    "type",
)
