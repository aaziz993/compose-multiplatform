package gradle.plugins.kover.reports.total

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverXmlTaskConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Configure Kover XML Report.
 *
 * Example:
 * ```
 * ...
 * xml {
 *     // Generate an XML report when running the `check` task
 *     onCheck = true
 *
 *     // XML report title (the location depends on the library)
 *     title = "Custom XML report title"
 *
 *     // Specify file to generate XML report
 *     xmlFile = layout.buildDirectory.file("my-xml-report.xml")
 * }
 *  ...
 * ```
 */
@Serializable
internal data class KoverXmlTaskConfig(
    /**
     * Generate an XML report when running the `check` task.
     *
     * `false` by default.
     */
    val onCheck: Boolean? = null,
    /**
     * File for saving generated XML report.
     *
     * `"${buildDirectory}/reports/kover/report${variantName}.xml"` by default.
     *
     * This value should not be hardcoded, it is always necessary to read the actual value from the property.
     */
    val xmlFile: String? = null,
    /**
     * Specify title in XML report.
     *
     * `"Kover Gradle Plugin XML report for $projectPath"` by default.
     */
    val title: String? = null
) {

    context(Project)
    fun applyTo(receiver: KoverXmlTaskConfig) {
        receiver.onCheck tryAssign onCheck
        receiver.xmlFile tryAssign xmlFile?.let(project::file)
        receiver.title tryAssign title
    }
}
