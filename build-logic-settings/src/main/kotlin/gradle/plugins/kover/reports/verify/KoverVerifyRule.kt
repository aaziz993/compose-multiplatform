package gradle.plugins.kover.reports.verify

import gradle.api.tryAssign
import kotlin.math.min
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.gradle.api.provider.Provider

@Serializable
internal data class KoverVerifyRule(
    /**
     * Specifies by which entity the code for separate coverage evaluation will be grouped.
     * [GroupingEntityType.APPLICATION] by default.
     */
    val groupBy: GroupingEntityType? = null,

    /**
     * Specifies that the rule is checked during verification.
     *
     * `false` by default.
     */
    val disabled: Boolean? = null,
    /**
     * Specifies the set of verification rules that control the
     * coverage conditions required for the verification task to pass.
     *
     * An example of bound configuration:
     * ```
     * // At least 75% of lines should be covered in order for build to pass
     * bound {
     *     aggregationForGroup = AggregationType.COVERED_PERCENTAGE // Default aggregation
     *     coverageUnits = CoverageUnit.LINE
     *     minValue = 75
     * }
     * ```
     *
     * @see KoverVerifyBound
     */
    val bound: KoverVerifyBound? = null,
    /**
     * A shortcut for
     * ```
     * bound {
     *     minValue = min
     * }
     * ```
     *
     * @see bound
     */
    val minBound: @Serializable(with = KoverVerifyBoundSerializer::class) Any? = null,
    /**
     * A shortcut for
     * ```
     * bound {
     *     maxValue = maxValue
     * }
     * ```
     *
     * @see bound
     */
    val maxBound: @Serializable(with = KoverVerifyBoundSerializer::class) Any? = null,
) {

    fun applyTo(recipient: kotlinx.kover.gradle.plugin.dsl.KoverVerifyRule) {
        recipient.groupBy tryAssign groupBy
        recipient.disabled tryAssign disabled

        bound?.let { bound ->
            recipient.bound(bound::applyTo)
        }

        when (minBound) {
            is Int -> recipient.minBound(minBound)
            is KoverVerifyBound -> recipient.minBound(minBound.minValue!!, minBound.coverageUnits!!, minBound.aggregationForGroup!!)
            else -> Unit
        }

        when (maxBound) {
            is Int -> recipient.maxBound(maxBound)
            is KoverVerifyBound -> recipient.maxBound(maxBound.minValue!!, maxBound.coverageUnits!!, maxBound.aggregationForGroup!!)
            else -> Unit
        }
    }
}

@Serializable
internal data class NamedKoverVerifyRule(
    val name: String,
    val rule: KoverVerifyRule,
)

internal object KoverVerifyRuleSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if ("name" in element.jsonObject) NamedKoverVerifyRule.serializer()
        else KoverVerifyRule.serializer()
}
