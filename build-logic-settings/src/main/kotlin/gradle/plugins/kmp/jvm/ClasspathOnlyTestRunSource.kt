package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: List<String>,
    val testClassesDirs: List<String>
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(run: KotlinJvmTestRun) =
        run.setExecutionSourceFrom(
            files(*classpath.toTypedArray()),
            files(* testClassesDirs.toTypedArray()),
        )
}
