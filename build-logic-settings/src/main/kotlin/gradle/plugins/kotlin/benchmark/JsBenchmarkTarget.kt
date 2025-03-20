package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import gradle.plugins.kmp.web.KotlinJsIrCompilation
import kotlinx.benchmark.gradle.JsBenchmarksExecutor
import kotlinx.benchmark.gradle.WasmBenchmarkTarget
import kotlinx.benchmark.gradle.internal.KotlinxBenchmarkPluginInternalApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class JsBenchmarkTarget : BenchmarkTarget() {

    abstract val compilation: KotlinJsIrCompilation?

    context(Project)
    @OptIn(KotlinxBenchmarkPluginInternalApi::class)
    override fun applyTo(recipient: kotlinx.benchmark.gradle.BenchmarkTarget) {
        super.applyTo(target)

        target as WasmBenchmarkTarget

        compilation?.applyTo(target.compilation)
    }
}

@Serializable
@SerialName("js")
internal data class JsBenchmarkTargetImpl(
    override val name: String = "",
    override val workingDir: String? = null,
    override val compilation: KotlinJsIrCompilation? = null,
    val jsBenchmarksExecutor: JsBenchmarksExecutor? = null,
) : JsBenchmarkTarget() {

    context(Project)
    override fun applyTo(recipient: kotlinx.benchmark.gradle.BenchmarkTarget) {
        super.applyTo(target)

        target as kotlinx.benchmark.gradle.JsBenchmarkTarget

        target::jsBenchmarksExecutor trySet jsBenchmarksExecutor
    }
}
