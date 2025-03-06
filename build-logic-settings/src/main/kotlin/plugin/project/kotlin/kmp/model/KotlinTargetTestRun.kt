package plugin.project.kotlin.kmp.model

import org.gradle.api.Named
import org.gradle.api.Project
import plugin.project.kotlin.model.KotlinExecution
import plugin.project.kotlin.model.KotlinTestRun

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
