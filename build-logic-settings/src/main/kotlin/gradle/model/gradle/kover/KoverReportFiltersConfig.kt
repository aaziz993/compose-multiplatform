package gradle.model.gradle.kover

import gradle.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverReportFiltersConfig
import kotlinx.serialization.Serializable

/**
 * Filters to exclude classes from reports
 */
@Serializable
internal data class KoverReportFiltersConfig(
    /**
     * Instance to configuring of class filter in order to exclude classes and functions.
     *
     * See details in [excludes].
     */
    val excludes: KoverReportFilter? = null,
    /**
     * Instance to configuring of class filter in order to include classes.
     *
     * See details in [includes].
     */
    val includes: KoverReportFilter? = null,
) {

    fun applyTo(filter: KoverReportFiltersConfig) {
        excludes?.let { excludes ->
            filter.excludes {
                classes tryAssign excludes.classes
                annotatedBy tryAssign excludes.annotatedBy
                projects tryAssign excludes.projects
                inheritedFrom tryAssign excludes.inheritedFrom
            }
        }
        includes?.let { includes ->
            filter.includes {
                classes tryAssign includes.classes
                annotatedBy tryAssign includes.annotatedBy
                projects tryAssign includes.projects
                inheritedFrom tryAssign includes.inheritedFrom
            }
        }
    }
}
