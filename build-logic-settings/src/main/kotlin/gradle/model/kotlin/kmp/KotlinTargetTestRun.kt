package gradle.model.kotlin.kmp

import gradle.model.kotlin.KotlinExecution
import gradle.model.kotlin.KotlinTestRun
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * A [KotlinTargetExecution] that executes configured tests in the context of a specific [KotlinTarget].
 */
internal interface KotlinTargetTestRun<ExecutionSource : KotlinExecution.ExecutionSource> :
    KotlinTestRun<ExecutionSource>,
    KotlinTargetExecution<ExecutionSource> {

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTestRun>.applyTo(named as org.jetbrains.kotlin.gradle.plugin.KotlinTestRun<*>)
    }
}
