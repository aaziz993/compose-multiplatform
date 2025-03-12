package gradle.plugins.kmp.jvm

import gradle.Named
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String = "",
    val executionSource: JvmClasspathTestRunSource? = null,
) : Named {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        named as KotlinJvmTestRun

        executionSource?.let { executionSource ->
            named.setExecutionSourceFrom(
                files(executionSource.classpath),
                files(executionSource.testClassesDirs),
            )
        }
    }
}
