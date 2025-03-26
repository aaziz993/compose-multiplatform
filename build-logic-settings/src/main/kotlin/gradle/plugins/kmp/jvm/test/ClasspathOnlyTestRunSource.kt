package gradle.plugins.kmp.jvm.test

import kotlinx.serialization.Serializable

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: Set<String>,
    val testClassesDirs: Set<String>
) : JvmClasspathTestRunSource
