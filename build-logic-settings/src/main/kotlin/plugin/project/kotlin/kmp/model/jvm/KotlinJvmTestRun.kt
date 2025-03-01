package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun
import plugin.project.kotlin.model.Named
import plugin.project.kotlin.model.configure

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String = "",
    val executionSource: JvmClasspathTestRunSource? = null,
) : Named {

    context(Project)
    fun applyTo(runs: NamedDomainObjectContainer<KotlinJvmTestRun>) = runs.configure {
        this@KotlinJvmTestRun.executionSource?.let { executionSource ->
            setExecutionSourceFrom(
                files(executionSource.classpath),
                files(executionSource.testClassesDirs),
            )
        }
    }
}
