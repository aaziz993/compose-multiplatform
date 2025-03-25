package gradle.plugins.kotlin.benchmark

import gradle.api.tasks.SourceSet
import kotlinx.benchmark.gradle.JavaBenchmarkTarget
import kotlinx.benchmark.gradle.internal.KotlinxBenchmarkPluginInternalApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
@SerialName("java")
internal data class JavaBenchmarkTarget(
    override val name: String? = null,
    override val workingDir: String? = null,
    val sourceSet: SourceSet? = null,
) : BenchmarkTarget<JavaBenchmarkTarget>() {

    context(project: Project)
    @OptIn(KotlinxBenchmarkPluginInternalApi::class)
    override fun applyTo(receiver: JavaBenchmarkTarget) {
        super.applyTo(receiver)

        sourceSet?.applyTo(receiver.sourceSet)
    }
}
