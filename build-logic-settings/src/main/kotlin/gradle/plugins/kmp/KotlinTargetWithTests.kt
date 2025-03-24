package gradle.plugins.kmp

import gradle.api.applyTo
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetWithTests

/**
 * Represents a [KotlinTarget] that includes test runs.
 */
internal interface KotlinTargetWithTests<T : KotlinTargetWithTests<*, *>> : KotlinTarget<T> {

    /**
     * The container that holds test run executions.
     *
     * A test run by the name [DEFAULT_TEST_RUN_NAME] is automatically created and configured.
     */
    val testRuns: Set<KotlinTargetTestRun<out org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun<*>>>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        testRuns?.forEach { testRun ->
            (testRun as KotlinTargetTestRun<org.jetbrains.kotlin.gradle.plugin.KotlinTargetTestRun<*>>).applyTo(receiver.testRuns)
        }
    }
}
