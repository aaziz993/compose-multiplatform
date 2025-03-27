package gradle.plugins.kotlin.targets.jvm.test

import kotlinx.serialization.Serializable

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: Set<String>,
    val testClassesDirs: Set<String>
) : JvmClasspathTestRunSource
