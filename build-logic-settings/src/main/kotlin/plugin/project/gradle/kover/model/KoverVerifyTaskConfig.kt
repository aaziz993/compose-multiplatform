package plugin.project.gradle.kover.model

import kotlinx.serialization.Serializable

/**
 * Configuration of the coverage's result verification with the specified rules.
 *
 * Example:
 * ```
 *  verify {
 *      onCheck = true
 *
 *      rule {
 *          // ...
 *      }
 *
 *      rule("Custom Name") {
 *          // ...
 *      }
 *
 *      // fail on verification error
 *      warningInsteadOfFailure = false
 *  }
 * ```
 */
@Serializable
internal data class KoverVerifyTaskConfig(
    /**
     * In case of a verification error, print a message to the log with the warn level instead of the Gradle task execution error.
     *
     * Gradle task error if `false`, warn message if `true`.
     *
     * `false` by default.
     */
    val warningInsteadOfFailure: Boolean? = null,
    /**
     * Verify coverage when running the `check` task.
     *
     * `true` for total verification of all code in the project, `false` otherwise.
     */
    public val onCheck: Boolean? = null
)
