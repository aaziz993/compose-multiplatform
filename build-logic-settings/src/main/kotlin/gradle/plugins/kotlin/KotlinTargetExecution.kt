package gradle.plugins.kotlin

import org.jetbrains.kotlin.gradle.plugin.KotlinTargetExecution

/**
 * Represents an execution in the scope of a [KotlinTarget].
 */
internal interface KotlinTargetExecution<T : KotlinTargetExecution<E>,
    E : org.jetbrains.kotlin.gradle.plugin.KotlinExecution.ExecutionSource> : KotlinExecution<T, E> {

}
