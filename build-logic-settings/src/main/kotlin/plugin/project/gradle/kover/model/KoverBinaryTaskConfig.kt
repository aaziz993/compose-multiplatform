package plugin.project.gradle.kover.model;

import gradle.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverBinaryTaskConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Configure Kover binary Report.
 *
 * Example:
 * ```
 * ...
 * binary {
 *     // Generate binary report when running the `check` task
 *     onCheck = true
 *
 *     // Specify file to generate binary report
 *     file = layout.buildDirectory.file("my-project-report/report.bin")
 * }
 *  ...
 * ```
 */
@Serializable
internal data class KoverBinaryTaskConfig(
    /**
     * Generate binary report when running the `check` task.
     *
     * `false` by default.
     */
    val onCheck: Boolean? = null,
    /**
     * Specify file to generate binary report.
     *
     * `"${buildDirectory}/reports/kover/report${variantName}.bin"` by default.
     *
     * This value should not be hardcoded, it is always necessary to read the actual value from the property.
     */
    val file: String? = null,
) {

    context(Project)
    fun applyTo(binary: KoverBinaryTaskConfig) {
        binary.onCheck tryAssign onCheck
        binary.file tryAssign file?.let(::file)
    }
}
