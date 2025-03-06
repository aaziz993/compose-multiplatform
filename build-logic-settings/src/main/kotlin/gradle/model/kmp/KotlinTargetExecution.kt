package gradle.model.kmp

import gradle.model.kotlin.KotlinExecution

/**
 * Represents an execution in the scope of a [KotlinTarget].
 */
internal interface KotlinTargetExecution<out SourceType : KotlinExecution.ExecutionSource> :
    KotlinExecution<SourceType> {

}
