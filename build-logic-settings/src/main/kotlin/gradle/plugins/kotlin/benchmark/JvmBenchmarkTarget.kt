package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class JvmBenchmarkTarget<T : kotlinx.benchmark.gradle.JvmBenchmarkTarget> : BenchmarkTarget<T>() {

    abstract val jmhVersion: String?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient::jmhVersion trySet jmhVersion
    }
}

@Serializable
@SerialName("jvm")
internal data class JvmBenchmarkTargetImpl(
    override val name: String? = null, ,
    override val workingDir: String? = null,
    override val jmhVersion: String? = null,
) : JvmBenchmarkTarget<kotlinx.benchmark.gradle.JvmBenchmarkTarget>()
