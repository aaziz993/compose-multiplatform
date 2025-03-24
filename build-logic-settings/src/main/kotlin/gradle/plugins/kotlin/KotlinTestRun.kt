package gradle.plugins.kotlin

import gradle.api.tasks.test.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinTestRun

/**
 * A [KotlinExecution] that runs configured tests.
 */
internal interface KotlinTestRun<T : KotlinTestRun<*>> : KotlinExecution<T> {

    /**
     * Configures filtering for executable tests using the provided [configureFilter] configuration.
     */
    val filter: TestFilter?

    @Suppress("UNCHECKED_CAST")
    fun applyTo(receiver: T) {
        filter?.let { filter ->
            receiver.filter(filter::applyTo)
        }
    }
}
