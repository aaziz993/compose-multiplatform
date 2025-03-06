package gradle.model.kotlin

import gradle.model.TestFilter
import org.jetbrains.kotlin.gradle.plugin.KotlinTestRun

/**
 * A [KotlinExecution] that runs configured tests.
 */
internal interface KotlinTestRun<out SourceType : KotlinExecution.ExecutionSource> : KotlinExecution<SourceType> {

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
