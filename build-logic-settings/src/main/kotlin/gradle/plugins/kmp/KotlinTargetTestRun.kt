package gradle.plugins.kmp

import gradle.plugins.kotlin.KotlinTestRun
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun

/**
 * A [KotlinTargetExecution] that executes configured tests in the context of a specific [KotlinTarget].
 */
internal interface KotlinTargetTestRun<T : KotlinTargetTestRun<*>> : KotlinTestRun<T>, KotlinTargetExecution<T> {

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<KotlinTestRun>.applyTo(receiver)
        super<KotlinTargetExecution>.applyTo(receiver)
    }
}
