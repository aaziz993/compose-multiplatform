package gradle.plugins.kotlin

import gradle.api.tasks.test.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinTestRun

/**
 * A [KotlinExecution] that runs configured tests.
 */
internal interface KotlinTestRun<T : KotlinTestRun<S>,
    S : org.jetbrains.kotlin.gradle.plugin.KotlinExecution.ExecutionSource,
    F : org.gradle.api.tasks.testing.TestFilter>
    : KotlinExecution<T, S> {

    /**
     * Configures filtering for executable tests using the provided [configureFilter] configuration.
     */
    val filter: TestFilter<F>?

    @Suppress("UNCHECKED_CAST")
    fun applyTo(recipient: T) {
        filter?.let { filter ->
            recipient.filter {
                filter.applyTo(this as F)
            }
        }
    }
}
