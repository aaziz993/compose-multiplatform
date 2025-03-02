package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun
import plugin.project.kotlin.model.Named

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String = "",
    val executionSource: JvmClasspathTestRunSource? = null,
) : Named {

    context(Project)
    fun applyTo(run: KotlinJvmTestRun) {
        executionSource?.let { executionSource ->
            run.setExecutionSourceFrom(
                files(executionSource.classpath),
                files(executionSource.testClassesDirs),
            )
        }
    }
}
