package gradle.plugins.kotlin

import gradle.api.tasks.test.TestFilter
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTestRun

/**
 * A [KotlinExecution] that runs configured tests.
 */
internal interface KotlinTestRun<
    T : KotlinTestRun<E>,
    E : org.jetbrains.kotlin.gradle.plugin.KotlinExecution.ExecutionSource
    > : KotlinExecution<T, E> {

    /**
     * Configures filtering for executable tests using the provided [configureFilter] configuration.
     */
    val filter: TestFilter?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun applyTo(receiver: T) {
        filter?.let { filter ->
            receiver.filter(filter::applyTo)
        }
    }
}
