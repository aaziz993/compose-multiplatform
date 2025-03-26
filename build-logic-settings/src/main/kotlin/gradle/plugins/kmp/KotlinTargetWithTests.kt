package gradle.plugins.kmp

import gradle.api.applyTo
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinExecution
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetWithTests

/**
 * Represents a [KotlinTarget] that includes test runs.
 */
internal interface KotlinTargetWithTests<T : KotlinTargetWithTests<E, R>,
    E : KotlinExecution.ExecutionSource,
    R : org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun<E>> : KotlinTarget<T> {

    /**
     * The container that holds test run executions.
     *
     * A test run by the name [DEFAULT_TEST_RUN_NAME] is automatically created and configured.
     */
    val testRuns: LinkedHashSet<out KotlinTargetTestRun<E, R>>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        testRuns?.forEach { testRun ->
            testRun.applyTo(receiver.testRuns)
        }
    }
}
