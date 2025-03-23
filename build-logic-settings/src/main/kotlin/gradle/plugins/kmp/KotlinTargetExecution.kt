package gradle.plugins.kmp

import gradle.plugins.kotlin.KotlinExecution
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetExecution

/**
 * Represents an execution in the scope of a [KotlinTarget].
 */
internal interface KotlinTargetExecution<T : KotlinTargetExecution<*>> : KotlinExecution<T> {

}
