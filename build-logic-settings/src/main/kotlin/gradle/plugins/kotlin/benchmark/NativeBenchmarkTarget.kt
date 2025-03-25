package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import kotlinx.benchmark.gradle.NativeBenchmarkTarget
import kotlinx.benchmark.gradle.internal.KotlinxBenchmarkPluginInternalApi
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class NativeBenchmarkTarget(
    override val name: String? = null,
    override val workingDir: String? = null,
    val buildType: NativeBuildType? = null,
    val compilation: KotlinNativeCompilation? = null,
) : BenchmarkTarget<NativeBenchmarkTarget>() {

    context(project: Project)
    @OptIn(KotlinxBenchmarkPluginInternalApi::class)
    override fun applyTo(receiver: NativeBenchmarkTarget) {
        super.applyTo(receiver)

        receiver::buildType trySet buildType
        compilation?.applyTo(receiver.compilation)
    }
}
