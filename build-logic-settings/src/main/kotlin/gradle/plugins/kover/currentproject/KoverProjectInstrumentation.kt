package gradle.plugins.kover.currentproject

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverProjectInstrumentation
import kotlinx.serialization.Serializable

/**
 * Instrumentation settings for the current Gradle project.
 *
 * Instrumentation is the modification of classes when they are loaded into the JVM, which helps to determine which code was called and which was not.
 * Instrumentation changes the bytecode of the class, so it may disable some JVM optimizations, slow down performance and concurrency tests, and may also be incompatible with other instrumentation libraries.
 *
 * For this reason, it may be necessary to fine-tune the instrumentation, for example, disabling instrumentation for problematic classes.
 *
 * Example:
 * ```
 *  instrumentation {
 *      // disable instrumentation of test tasks of all classes
 *      disabledForAll = true
 *
 *      // The coverage of the test1 and test2 tasks will no longer be taken into account in the reports
 *      //  as well as these tasks will not be called when generating the report.
 *      // These tasks will not be instrumented even if you explicitly run them
 *      disabledForTestTasks.addAll("test1", "test2")
 *
 *      // disable instrumentation of specified classes in test tasks
 *      excludedClasses.addAll("foo.bar.*Biz", "*\$Generated")
 *  }
 * ```
 */
@Serializable
internal data class KoverProjectInstrumentation(
    /**
     * Disable instrumentation in all test tasks.
     * No one task from this project will not be called when generating Kover reports and these tasks will not be instrumented even if you explicitly run them.
     */
    val disabledForAll: Boolean? = null,
    /**
     * Specifies not to use test task with passed names to measure coverage.
     * These tasks will also not be called when generating Kover reports and these tasks will not be instrumented even if you explicitly run them.
     */
    val disabledForTestTasks: Set<String>? = null,
    /**
     * Disable instrumentation in test tasks of specified classes
     *
     * Classes in [excludedClasses] have priority over classes from [includedClasses].
     */
    val excludedClasses: Set<String>? = null,
    /**
     * Enable instrumentation in test tasks only of specified classes.
     * All other classes will not be instrumented.
     *
     * Classes in [excludedClasses] have priority over classes from [includedClasses].
     */
    val includedClasses: Set<String>? = null,
) {

    fun applyTo(recipient: KoverProjectInstrumentation) {
        recipient.disabledForAll tryAssign disabledForAll
        recipient.disabledForTestTasks tryAssign disabledForTestTasks
        recipient.excludedClasses tryAssign excludedClasses
        recipient.includedClasses tryAssign includedClasses
    }
}
