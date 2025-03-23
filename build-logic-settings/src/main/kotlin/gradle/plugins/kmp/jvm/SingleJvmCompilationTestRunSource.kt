package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal class SingleJvmCompilationTestRunSource(
    val compilation: String
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(receiver: KotlinJvmTestRun) =
        receiver.setExecutionSourceFrom(
            receiver.target.compilations.getByName(compilation),
        )
}
