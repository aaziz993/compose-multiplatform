package gradle.plugins.kotlin.targets.jvm.test

import kotlinx.serialization.Serializable

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource
