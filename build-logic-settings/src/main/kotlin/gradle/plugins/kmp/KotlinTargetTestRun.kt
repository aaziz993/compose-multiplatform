package gradle.plugins.kmp

import gradle.plugins.kotlin.KotlinTestRun
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinExecution
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun

/**
 * A [KotlinTargetExecution] that executes configured tests in the context of a specific [KotlinTarget].
 */
internal interface KotlinTargetTestRun<
    T : KotlinTargetTestRun<S>,
    S : KotlinExecution.ExecutionSource,
    F : TestFilter>
    : KotlinTestRun<T, S, F>, KotlinTargetExecution<T, S> {

    context(Project)
    override fun applyTo(recipient: T) =
        super<KotlinTestRun>.applyTo(recipient)
}
