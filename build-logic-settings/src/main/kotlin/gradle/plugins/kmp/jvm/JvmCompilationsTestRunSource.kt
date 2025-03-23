package gradle.plugins.kmp.jvm

import gradle.api.getByNameOrAll
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class JvmCompilationsTestRunSource(
    val classpathCompilations: Set<String>,
    val testCompilations: Set<String>
) : JvmClasspathTestRunSource {

    context(Project)
    override fun applyTo(receiver: KotlinJvmTestRun) =
        receiver.setExecutionSourceFrom(
            classpathCompilations.flatMap(receiver.target.compilations::getByNameOrAll),
            testCompilations.flatMap(receiver.target.compilations::getByNameOrAll),
        )
}
