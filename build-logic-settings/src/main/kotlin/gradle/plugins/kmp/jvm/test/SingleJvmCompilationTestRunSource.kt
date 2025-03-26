package gradle.plugins.kmp.jvm.test

import kotlinx.serialization.Serializable

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource
