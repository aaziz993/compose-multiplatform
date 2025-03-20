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
    override val name: String = "",
    override val workingDir: String? = null,
    val buildType: NativeBuildType? = null,
    val compilation: KotlinNativeCompilation? = null,
) : BenchmarkTarget() {

    context(Project)
    @OptIn(KotlinxBenchmarkPluginInternalApi::class)
    override fun applyTo(recipient: kotlinx.benchmark.gradle.BenchmarkTarget) {
        super.applyTo(target)

        target as NativeBenchmarkTarget

        target::buildType trySet buildType

        compilation?.applyTo(target.compilation)
    }
}
