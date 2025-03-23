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
    override fun applyTo(recipient: KotlinJvmTestRun) =
        recipient.setExecutionSourceFrom(
            classpathCompilations.flatMap(recipient.target.compilations::getByNameOrAll),
            testCompilations.flatMap(recipient.target.compilations::getByNameOrAll),
        )
}
