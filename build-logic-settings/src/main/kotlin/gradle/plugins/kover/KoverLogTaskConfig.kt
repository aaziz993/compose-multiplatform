package gradle.plugins.kover

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import kotlinx.kover.gradle.plugin.dsl.KoverLogTaskConfig
import kotlinx.serialization.Serializable

/**
 * Configuration of coverage printing to the log for current report variant.
 * ```
 *  log {
 *      onCheck = true
 *
 *      filters {
 *          // ...
 *      }
 *      header = null
 *      format = "<entity> line coverage: <value>%"
 *      groupBy = GroupingEntityType.APPLICATION
 *      coverageUnits = CoverageUnit.LINE
 *      aggregationForGroup = AggregationType.COVERED_PERCENTAGE
 *  }
 * ```
 */
@Serializable
internal data class KoverLogTaskConfig(
    /**
     * Print coverage when running the `check` task.
     *
     * `false` by default.
     */
    val onCheck: Boolean? = null,
    /**
     * Add a header line to the output before the lines with coverage.
     *
     * Absent by default.
     */
    val header: String? = null,
    /**
     * Format of the strings to print coverage for the specified in [groupBy] group.
     *
     * The following placeholders can be used:
     *  - `<value>` - coverage value
     *  - `<entity>` - name of the entity by which the grouping took place. `application` if [groupBy] is [GroupingEntityType.APPLICATION].
     *
     * `"<entity> line coverage: <value>%"` by default.
     */
    val format: String? = null,
    /**
     * Specifies by which entity the code for separate coverage evaluation will be grouped.
     *
     *
     * [GroupingEntityType.APPLICATION] by default.
     */
    val groupBy: GroupingEntityType? = null,
    /**
     * The type of application code division (unit type) whose unit coverage will be considered independently.
     *
     * [CoverageUnit.LINE] by default.
     */
    val coverageUnits: CoverageUnit? = null,
    /**
     * Specifies aggregation function that will be calculated over all the units of the same group.
     *
     * This function used to calculate the aggregated coverage value, it uses the values of the covered and uncovered units of type [coverageUnits] as arguments.
     *
     * Result value will be printed.
     *
     * [AggregationType.COVERED_PERCENTAGE] by default.
     */
    val aggregationForGroup: AggregationType? = null
) {

    fun applyTo(log: KoverLogTaskConfig) {
        log.onCheck tryAssign onCheck
        log.header tryAssign header
        log.format tryAssign format
        log.groupBy tryAssign groupBy
        log.coverageUnits tryAssign coverageUnits
        log.aggregationForGroup tryAssign aggregationForGroup
    }
}
