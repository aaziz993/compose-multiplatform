package plugin.project.kmp.model

import plugin.project.kotlin.model.KotlinExecution

/**
 * Represents an execution in the scope of a [KotlinTarget].
 */
internal interface KotlinTargetExecution<out SourceType : KotlinExecution.ExecutionSource> :
    KotlinExecution<SourceType> {

}
