package gradle.plugins.kover

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverVerificationRulesConfig
import kotlinx.serialization.Serializable

/**
 * Configuration to specify verification rules.
 *
 * Example:
 * ```
 *  verify {
 *      rule {
 *          // verification rule
 *      }
 *
 *      rule("custom rule name") {
 *          // named verification rule
 *      }
 *
 *      // fail on verification error
 *      warningInsteadOfFailure = false
 *  }
 * ```
 */
@Serializable
internal data class KoverVerificationRulesConfig(
    /**
     * In case of a verification error, print a message to the log with the warn level instead of the Gradle task execution error.
     *
     * Gradle task error if `false`, warn message if `true`.
     *
     * `false` by default.
     */
    val warningInsteadOfFailure: Boolean? = null
) {

    fun applyTo(recipient: KoverVerificationRulesConfig) {
        rules.warningInsteadOfFailure tryAssign warningInsteadOfFailure
    }
}
