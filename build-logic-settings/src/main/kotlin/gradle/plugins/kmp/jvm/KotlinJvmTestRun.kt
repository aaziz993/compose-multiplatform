package gradle.plugins.kmp.jvm

import gradle.plugins.kmp.KotlinTaskTestRun
import gradle.plugins.kotlin.KotlinExecution
import gradle.api.tasks.test.DefaultTestFilter
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String = "",
    override val filter: DefaultTestFilter? = null,
    override val executionSource: JvmClasspathTestRunSource? = null,
) : KotlinTaskTestRun() {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        super.applyTo(named)

        named as KotlinJvmTestRun

        executionSource?.applyTo(named)
    }
}
