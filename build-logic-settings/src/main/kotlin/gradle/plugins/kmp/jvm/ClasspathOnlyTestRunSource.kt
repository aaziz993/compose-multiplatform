package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal class ClasspathOnlyTestRunSource(
    val classpath: Set<String>,
    val testClassesDirs: Set<String>
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(recipient: KotlinJvmTestRun) =
        recipient.setExecutionSourceFrom(
            files(*classpath.toTypedArray()),
            files(* testClassesDirs.toTypedArray()),
        )
}
