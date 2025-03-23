package gradle.plugins.kover.reports.verify

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

/**
 * Describes a single bound for the verification rule to enforce;
 * Bound specifies what type of coverage is enforced (branches, lines, instructions),
 * how coverage is aggregated (raw number or percents) and what numerical values of coverage
 * are acceptable.
 */
@Serializable
internal data class KoverVerifyBound(
    /**
     * Specifies minimal value to compare with aggregated coverage value.
     * The comparison occurs only if the value is present.
     *
     * Absent by default.
     */
    val minValue: Int? = null,
    /**
     * Specifies maximal value to compare with counter value.
     * The comparison occurs only if the value is present.
     *
     * Absent by default.
     */
    val maxValue: Int? = null,
    /**
     * The type of application code division (unit type) whose unit coverage will be considered independently.
     * It affects which blocks the value of the covered and missed units will be calculated for.
     *
     * [CoverageUnit.LINE] by default.
     */
    val coverageUnits: CoverageUnit? = null,
    /**
     * Specifies aggregation function that will be calculated over all the units of the same group.
     *
     * This function used to calculate the aggregated coverage value, it uses the values of the covered and uncovered units of type [coverageUnits] as arguments.
     *
     * Result value will be compared with the bounds.
     *
     * [AggregationType.COVERED_PERCENTAGE] by default.
     */
    val aggregationForGroup: AggregationType? = null,
) {

    fun applyTo(receiver: kotlinx.kover.gradle.plugin.dsl.KoverVerifyBound) {
        receiver.minValue tryAssign minValue
        receiver.maxValue tryAssign maxValue
        receiver.coverageUnits tryAssign coverageUnits
        receiver.aggregationForGroup tryAssign aggregationForGroup
    }
}

internal object KoverVerifyBoundSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) Int.serializer()
        else KoverVerifyBound.serializer()
}
