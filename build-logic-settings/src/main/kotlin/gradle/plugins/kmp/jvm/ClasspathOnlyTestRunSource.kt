package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: List<String>,
    val testClassesDirs: List<String>
) : JvmClasspathTestRunSource
