package gradle.model.kmp.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun
import gradle.model.Named

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
