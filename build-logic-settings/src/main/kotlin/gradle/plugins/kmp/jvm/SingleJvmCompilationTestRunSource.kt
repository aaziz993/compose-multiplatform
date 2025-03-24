package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource
