package plugin.project.gradle.kover.model

import kotlinx.serialization.Serializable

/**
 * Type to configure report set for a specific variant
 *
 * example:
 * ```
 *  filters {
 *      // override report filters
 *  }
 *  html {
 *      // configure HTML report
 *  }
 *  xml {
 *      // configure XML report
 *  }
 *  verify {
 *      // configure coverage verification
 *  }
 *
 *  additionalBinaryReports.add(file("path/to/the/file.ic"))
 * ```
 */
@Serializable
internal data class KoverReportSetConfig(
    /**
     * Instance to configuring of common report filters.
     *
     * See details in [filters].
     */
    val filters: KoverReportFiltersConfig? = null,
    /**
     * Instance to configuring of HTML report for current report variant.
     *
     * See details in [html].
     */
    val html: KoverHtmlTaskConfig? = null,
    /**
     * Instance to configuring of XML report for current report variant.
     *
     * See details in [xml].
     */
    val xml: KoverXmlTaskConfig? = null,
    /**
     * Instance to configuring of Kover binary report for current report variant.
     *
     * See details in [binary].
     */
    val binary: KoverBinaryTaskConfig? = null,
    /**
     * Instance to configuring of coverage verification for current report variant.
     *
     * See details in [verify].
     */
    val verify: KoverVerifyTaskConfig? = null,
    /**
     * Instance to configuring of  coverage printing to the log for current report variant.
     *
     * See details in [log].
     */
    val log: KoverLogTaskConfig? = null,

    /**
     * Use coverage from external files in binary IC format.
     *
     * Coverage results from specified binary reports will be included in produced Kover reports.
     */
    val additionalBinaryReports: Set<String>? = null
)
