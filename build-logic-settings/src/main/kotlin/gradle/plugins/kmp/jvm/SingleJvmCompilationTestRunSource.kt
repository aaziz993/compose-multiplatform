package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(run: KotlinJvmTestRun) =
        run.setExecutionSourceFrom(
            run.target.compilations.getByName(compilation),
        )
}
