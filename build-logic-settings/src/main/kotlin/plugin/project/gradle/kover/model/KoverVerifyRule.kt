package plugin.project.gradle.kover.model

import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import kotlinx.serialization.Serializable

@Serializable
internal data class KoverVerifyRule(
    /**
     * Specifies by which entity the code for separate coverage evaluation will be grouped.
     * [GroupingEntityType.APPLICATION] by default.
     */
    public val groupBy: GroupingEntityType?=null,

    /**
     * Specifies that the rule is checked during verification.
     *
     * `false` by default.
     */
    public val disabled: Boolean? = null
)
