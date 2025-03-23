package gradle.plugins.kover.reports.verify

import gradle.api.tryAssign
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
internal interface KoverVerificationRulesConfig<T : kotlinx.kover.gradle.plugin.dsl.KoverVerificationRulesConfig> {

    /**
     * Add new coverage verification rule to check after test task execution.
     */
    val rule: @Serializable(with = KoverVerifyRuleSerializer::class) Any?

    /**
     * In case of a verification error, print a message to the log with the warn level instead of the Gradle task execution error.
     *
     * Gradle task error if `false`, warn message if `true`.
     *
     * `false` by default.
     */
    val warningInsteadOfFailure: Boolean?

    fun applyTo(receiver: T) {
        when (val rule = rule) {
            is KoverVerifyRule -> receiver.rule(rule::applyTo)
            is NamedKoverVerifyRule -> receiver.rule(rule.name, rule.rule::applyTo)
            else -> Unit
        }

        receiver.warningInsteadOfFailure tryAssign warningInsteadOfFailure
    }
}

@Serializable
internal data class KoverVerificationRulesConfigImpl(
    override val rule: @Serializable(with = KoverVerifyRuleSerializer::class) Any? = null,
    override val warningInsteadOfFailure: Boolean? = null
) : KoverVerificationRulesConfig<kotlinx.kover.gradle.plugin.dsl.KoverVerificationRulesConfig>
