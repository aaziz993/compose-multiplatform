package plugin.project.kmp.model

import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetWithTests
import plugin.project.kotlin.model.KotlinExecution

/**
 * Represents a [KotlinTarget] that includes test runs.
 */
internal interface KotlinTargetWithTests<E : KotlinExecution.ExecutionSource, T : KotlinTargetTestRun<E>> : KotlinTarget {

    /**
     * The container that holds test run executions.
     *
     * A test run by the name [DEFAULT_TEST_RUN_NAME] is automatically created and configured.
     */
    val testRuns: List<T>?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinTargetWithTests<*, *>

        testRuns?.forEach { testRun ->
            testRun.applyTo(named.testRuns)
        }
    }
}
