package plugin.project.gradle.kover.model

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
     val excludes: KoverReportFilter?=null,
    /**
     * Instance to configuring of class filter in order to include classes.
     *
     * See details in [includes].
     */
    public val includes: KoverReportFilter?=null,
)
