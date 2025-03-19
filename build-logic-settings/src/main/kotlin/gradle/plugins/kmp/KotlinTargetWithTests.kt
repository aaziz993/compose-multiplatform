package gradle.plugins.kmp


import org.gradle.api.Named
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetWithTests

/**
 * Represents a [KotlinTarget] that includes test runs.
 */
internal interface KotlinTargetWithTests<T : KotlinTargetTestRun> : KotlinTarget {

    /**
     * The container that holds test run executions.
     *
     * A test run by the name [DEFAULT_TEST_RUN_NAME] is automatically created and configured.
     */
    val testRuns: List<T>?

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as KotlinTargetWithTests<*, *>

        testRuns?.forEach { testRun ->
            testRun.applyTo(named.testRuns)
        }
    }
}
