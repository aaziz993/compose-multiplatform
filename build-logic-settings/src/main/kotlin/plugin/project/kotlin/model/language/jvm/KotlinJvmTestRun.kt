package plugin.project.kotlin.model.language.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class KotlinJvmTestRun(
    val name: String,
    val executionSource: JvmClasspathTestRunSource? = null,
) {

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
