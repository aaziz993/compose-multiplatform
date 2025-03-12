package gradle.plugins.kmp

import gradle.plugins.kotlin.KotlinExecution

/**
 * Represents an execution in the scope of a [KotlinTarget].
 */
internal interface KotlinTargetExecution<out SourceType : KotlinExecution.ExecutionSource> :
    KotlinExecution<SourceType> {

}
