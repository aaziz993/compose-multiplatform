package gradle.plugins.kotlin

import gradle.api.tasks.test.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinTestRun

/**
 * A [KotlinExecution] that runs configured tests.
 */
internal interface KotlinTestRun : KotlinExecution {

    /**
     * Configures filtering for executable tests using the provided [configureFilter] configuration.
     */
    val filter: TestFilter?

    fun applyTo(run: KotlinTestRun<*>) {
        filter?.let { filter ->
            run.filter(filter::applyTo)
        }
    }
}
