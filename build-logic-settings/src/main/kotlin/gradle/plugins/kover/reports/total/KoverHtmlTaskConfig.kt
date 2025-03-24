package gradle.plugins.kover.reports.total

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverHtmlTaskConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Configure Kover HTML Report.
 *
 * Example:
 * ```
 * ...
 * html {
 *     title = "Custom title"
 *
 *     // Generate an HTML report when running the `check` task
 *     onCheck = false
 *
 *     // Specify HTML report directory
 *     htmlDir = layout.buildDirectory.dir("my-html-report")
 * }
 *  ...
 * ```
 */
@Serializable
internal data class KoverHtmlTaskConfig(
    /**
     * Specify header in HTML reports.
     *
     * If not specified, project path is used instead.
     */
    val title: String? = null,
    /**
     * Specify charset in HTML reports.
     *
     * If not specified, used return value of `Charset.defaultCharset()` for Kover report generator and UTF-8 is used for JaCoCo.
     */
    val charset: String? = null,
    /**
     * Generate an HTML report when running the `check` task.
     *
     * `false` by default.
     */
    val onCheck: Boolean? = null,
    /**
     * HTML report directory.
     *
     * `"${buildDirectory}/reports/kover/html${variantName}"` by default.
     *
     * This value should not be hardcoded, it is always necessary to read the actual value from the property.
     */
    val htmlDir: String? = null,
) {

    context(project: Project)
    fun applyTo(receiver: KoverHtmlTaskConfig) {
        receiver.title tryAssign title
        receiver.charset tryAssign charset
        receiver.onCheck tryAssign onCheck
        receiver.htmlDir tryAssign htmlDir?.let(project.layout.projectDirectory::dir)
    }
}
