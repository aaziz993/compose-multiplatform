package gradle.plugins.kotlin.benchmark

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jvm")
internal data class KotlinJvmBenchmarkTarget(
    override val name: String = "",
    override val workingDir: String? = null,
    override val jmhVersion: String? = null,
) : JvmBenchmarkTarget()
