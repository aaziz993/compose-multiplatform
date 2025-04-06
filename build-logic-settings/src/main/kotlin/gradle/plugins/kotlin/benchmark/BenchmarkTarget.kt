package gradle.plugins.kotlin.benchmark

import gradle.api.ProjectNamed
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = BenchmarkTargetObjectTransformingContentPolymorphicSerializer::class)
internal abstract class BenchmarkTarget<T : kotlinx.benchmark.gradle.BenchmarkTarget> : ProjectNamed<T> {

    abstract val workingDir: String?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::workingDir trySet workingDir
    }
}

private class BenchmarkTargetObjectTransformingContentPolymorphicSerializer(serializer: KSerializer<Nothing>) : JsonObjectTransformingContentPolymorphicSerializer<BenchmarkTarget<*>>(
    BenchmarkTarget::class,
)
