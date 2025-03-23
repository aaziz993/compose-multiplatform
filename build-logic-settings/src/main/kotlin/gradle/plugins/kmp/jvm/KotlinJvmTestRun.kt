package gradle.plugins.kmp.jvm

import gradle.api.tasks.test.DefaultTestFilter
import gradle.plugins.kmp.KotlinTaskTestRun
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

@Serializable
internal data class KotlinJvmTestRun(
    override val name: String? = null,
    override val filter: DefaultTestFilter? = null,
    override val executionSource: JvmClasspathTestRunSource? = null,
) : KotlinTaskTestRun() {

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(named)

        named as KotlinJvmTestRun

        executionSource?.applyTo(named)
    }
}
