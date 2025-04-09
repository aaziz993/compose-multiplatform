package gradle.plugins.kover.reports.verify

import gradle.api.provider.tryAssign
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import klib.data.type.serialization.serializer.ContentPolymorphicSerializer

import kotlinx.serialization.json.jsonObject

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
    val minBound: @Serializable(with = KoverVerifyBoundContentPolymorphicSerializer::class) Any? = null,
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
    val maxBound: @Serializable(with = KoverVerifyBoundContentPolymorphicSerializer::class) Any? = null,
) {

    fun applyTo(receiver: kotlinx.kover.gradle.plugin.dsl.KoverVerifyRule) {
        receiver.groupBy tryAssign groupBy
        receiver.disabled tryAssign disabled

        bound?.let { bound ->
            receiver.bound(bound::applyTo)
        }

        when (minBound) {
            is Int -> receiver.minBound(minBound)
            is KoverVerifyBound -> receiver.minBound(minBound.minValue!!, minBound.coverageUnits!!, minBound.aggregationForGroup!!)
            else -> Unit
        }

        when (maxBound) {
            is Int -> receiver.maxBound(maxBound)
            is KoverVerifyBound -> receiver.maxBound(maxBound.minValue!!, maxBound.coverageUnits!!, maxBound.aggregationForGroup!!)
            else -> Unit
        }
    }
}

@Serializable
internal data class NamedKoverVerifyRule(
    val name: String,
    val rule: KoverVerifyRule,
)

internal object KoverVerifyRuleContentPolymorphicSerializer : ContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(value: Any?): DeserializationStrategy<Any> =
        if ("name" in element.jsonObject) NamedKoverVerifyRule.serializer()
        else KoverVerifyRule.serializer()
}
