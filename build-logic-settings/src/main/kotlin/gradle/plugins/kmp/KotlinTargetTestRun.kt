package gradle.plugins.kmp

import gradle.plugins.kotlin.KotlinTestRun
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun

/**
 * A [KotlinTargetExecution] that executes configured tests in the context of a specific [KotlinTarget].
 */
internal interface KotlinTargetTestRun<T : KotlinTargetTestRun<*>> : KotlinTestRun<T>, KotlinTargetExecution<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinTestRun>.applyTo(receiver)
        super<KotlinTargetExecution>.applyTo(receiver)
    }
}
