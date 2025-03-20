package gradle.plugins.kotlin.benchmark

import gradle.plugins.java.SourceSet
import kotlinx.benchmark.gradle.JavaBenchmarkTarget
import kotlinx.benchmark.gradle.internal.KotlinxBenchmarkPluginInternalApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
@SerialName("java")
internal data class JavaBenchmarkTarget(
    override val name: String = "",
    override val workingDir: String? = null,
    val sourceSet: SourceSet? = null,
) : BenchmarkTarget() {

    context(Project)
    @OptIn(KotlinxBenchmarkPluginInternalApi::class)
    override fun applyTo(recipient: kotlinx.benchmark.gradle.BenchmarkTarget) {
        super.applyTo(target)

        target as JavaBenchmarkTarget

        sourceSet?.applyTo(target.sourceSet)
    }
}
