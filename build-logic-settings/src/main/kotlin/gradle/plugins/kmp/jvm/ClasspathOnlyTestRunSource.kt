package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: Set<String>,
    val testClassesDirs: Set<String>
) : JvmClasspathTestRunSource
