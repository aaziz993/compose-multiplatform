package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource
