package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable

@Serializable
internal data class JvmCompilationsTestRunSource(
    val classpathCompilations: List<String>,
    val testCompilations: List<String>
) : KotlinExecution.ExecutionSource
