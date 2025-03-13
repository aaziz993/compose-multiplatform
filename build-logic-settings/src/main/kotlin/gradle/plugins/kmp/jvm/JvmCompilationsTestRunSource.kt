package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class JvmCompilationsTestRunSource(
    val classpathCompilations: List<String>,
    val testCompilations: List<String>
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(run: KotlinJvmTestRun) =
        run.setExecutionSourceFrom(
            classpathCompilations.map(run.target.compilations::getByName),
            testCompilations.map(run.target.compilations::getByName),
        )
}
