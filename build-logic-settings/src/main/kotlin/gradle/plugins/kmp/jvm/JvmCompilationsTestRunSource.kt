package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable

@Serializable
internal data class JvmCompilationsTestRunSource(
    val classpathCompilations: Set<String>,
    val testCompilations: Set<String>
) : JvmClasspathTestRunSource
