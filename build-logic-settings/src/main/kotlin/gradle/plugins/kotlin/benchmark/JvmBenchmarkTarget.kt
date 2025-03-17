package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class JvmBenchmarkTarget : BenchmarkTarget() {

    abstract val jmhVersion: String?

    context(Project)
    override fun applyTo(target: kotlinx.benchmark.gradle.BenchmarkTarget) {
        super.applyTo(target)

        target as kotlinx.benchmark.gradle.JvmBenchmarkTarget

        target::jmhVersion trySet jmhVersion
    }
}

@Serializable
@SerialName("jvm")
internal data class JvmBenchmarkTargetImpl(
    override val name: String = "",
    override val workingDir: String? = null,
    override val jmhVersion: String? = null,
) : JvmBenchmarkTarget()
