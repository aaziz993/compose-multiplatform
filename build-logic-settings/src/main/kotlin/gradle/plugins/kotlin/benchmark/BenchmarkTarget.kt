package gradle.plugins.kotlin.benchmark

import gradle.api.ProjectNamed
import gradle.api.trySet
import gradle.serialization.serializer.JsonKeyValueTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = BenchmarkTargetKeyValueTransformingContentPolymorphicSerializer::class)
internal abstract class BenchmarkTarget<T : kotlinx.benchmark.gradle.BenchmarkTarget> : ProjectNamed<T> {

    abstract val workingDir: String?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::workingDir trySet workingDir
    }
}

private object BenchmarkTargetKeyValueTransformingContentPolymorphicSerializer : JsonKeyValueTransformingContentPolymorphicSerializer<BenchmarkTarget<*>>(
    BenchmarkTarget::class,
)
