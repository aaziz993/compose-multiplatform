package gradle.plugins.kover.reports.total

import gradle.api.tryAssign
import gradle.plugins.kover.reports.verify.KoverVerificationRulesConfig
import gradle.plugins.kover.reports.verify.KoverVerifyRuleContentPolymorphicSerializer
import gradle.plugins.kover.reports.verify.KoverVerifyRuleSerializer
import kotlinx.kover.gradle.plugin.dsl.KoverVerifyTaskConfig
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
    override val rule: @Serializable(with = KoverVerifyRuleContentPolymorphicSerializer::class) Any? = null,
    override val warningInsteadOfFailure: Boolean? = null,
    /**
     * Verify coverage when running the `check` task.
     *
     * `true` for total verification of all code in the project, `false` otherwise.
     */
    val onCheck: Boolean? = null,
) : KoverVerificationRulesConfig<KoverVerifyTaskConfig> {

    override fun applyTo(receiver: KoverVerifyTaskConfig) {
        super.applyTo(receiver)

        receiver.onCheck tryAssign onCheck
    }
}
