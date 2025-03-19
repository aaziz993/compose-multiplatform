package gradle.plugins.kotlin.benchmark

import gradle.plugins.kmp.web.KotlinJsIrCompilation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wasm")
internal data class WasmBenchmarkTarget(
    override val name: String = "",
    override val workingDir: String? = null,
    override val compilation: KotlinJsIrCompilation? = null,
) : JsBenchmarkTarget()
