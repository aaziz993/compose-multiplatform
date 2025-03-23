package gradle.plugins.kmp.jvm

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

    context(Project)
    override fun applyTo(receiver: KotlinJvmTestRun) {
        super.applyTo(receiver)

        executionSource?.applyTo(receiver)
    }
}
