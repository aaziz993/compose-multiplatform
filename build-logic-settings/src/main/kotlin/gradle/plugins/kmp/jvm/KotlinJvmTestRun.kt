package gradle.plugins.kmp.jvm

import gradle.api.getByNameOrAll
import gradle.api.tasks.test.TestFilter
import gradle.plugins.kmp.KotlinTaskTestRun
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String? = null,
    override val filter: TestFilter? = null,
    override val executionSource: JvmClasspathTestRunSource? = null,
) : KotlinTaskTestRun<KotlinJvmTestRun>() {

    context(project: Project)
    override fun applyTo(receiver: KotlinJvmTestRun) {
        super.applyTo(receiver)

        when (executionSource) {
            is ClasspathOnlyTestRunSource -> receiver.setExecutionSourceFrom(
                files(*executionSource.classpath.toTypedArray()),
                files(* executionSource.testClassesDirs.toTypedArray()),
            )

            is SingleJvmCompilationTestRunSource -> receiver.setExecutionSourceFrom(
                receiver.target.compilations.getByName(executionSource.compilation),
            )

            is JvmCompilationsTestRunSource -> receiver.setExecutionSourceFrom(
                executionSource.classpathCompilations.flatMap(receiver.target.compilations::getByNameOrAll),
                executionSource.testCompilations.flatMap(receiver.target.compilations::getByNameOrAll),
            )

            else -> Unit
        }
    }
}
