package gradle.plugins.kover.reports.total

import gradle.api.tryAssign
import gradle.plugins.kover.reports.filters.KoverReportFiltersConfig
import kotlinx.kover.gradle.plugin.dsl.KoverReportSetConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
     * Specify common report filters, these filters will be inherited in HTML/XML/verification and other reports.
     *
     * Using this block will add additional filters to those that were inherited and specified earlier.
     * In order to clear the existing filters, use [filters].
     *
     * ```
     *  filtersAppend {
     *      excludes {
     *          // ...
     *      }
     *
     *      includes {
     *          // ...
     *      }
     *  }
     * ```
     */
    val filterAppends: Set<KoverReportFiltersConfig>? = null,
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
     * Configure coverage verification for current report variant.
     *
     * Using this block will add additional bounds to those that were inherited and specified earlier.
     * In order to clear the existing bounds, use [verify].
     *
     * ```
     *  verifyAppend {
     *      onCheck = true
     *
     *      rule {
     *          // ...
     *      }
     *
     *      rule("Custom Name") {
     *          // ...
     *      }
     *  }
     * ```
     */
    val verifyAppends: Set<KoverVerifyTaskConfig>? = null,
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
) {

    context(Project)
    fun applyTo(receiver: KoverReportSetConfig) {
        filters?.applyTo(receiver.filters)

        filterAppends?.forEach { filterAppend ->
            receiver.filtersAppend(filterAppend::applyTo)
        }

        html?.applyTo(receiver.html)
        xml?.applyTo(receiver.xml)
        binary?.applyTo(receiver.binary)
        verify?.applyTo(receiver.verify)

        verifyAppends?.forEach { filterAppend ->
            receiver.verifyAppend(filterAppend::applyTo)
        }

        log?.applyTo(receiver.log)
        receiver.additionalBinaryReports tryAssign additionalBinaryReports?.map(project::file)
    }
}
