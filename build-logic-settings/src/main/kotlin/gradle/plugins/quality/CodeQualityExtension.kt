package gradle.plugins.quality

import gradle.accessors.sourceSets
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeQualityExtension

/**
 * Base Code Quality Extension.
 */
internal abstract class CodeQualityExtension {

    /**
     * The version of the code quality tool to be used.
     */
    abstract val toolVersion: String?

    /**
     * The source sets to be analyzed as part of the `check` and `build` tasks.
     */
    abstract val sourceSets: List<String>?

    /**
     * Whether to allow the build to continue if there are warnings.
     *
     * Example: ignoreFailures = true
     */
    abstract val ignoreFailures: Boolean?

    /**
     * The directory where reports will be generated.
     */
    abstract val reportsDir: String?

    context(Project)
    fun applyTo(extension: CodeQualityExtension) {
        toolVersion?.let(extension::setToolVersion)
        sourceSets?.map(project.sourceSets::getByName)?.let(extension::setSourceSets)
        ignoreFailures?.let(extension::setIgnoreFailures)
        reportsDir?.let(::file)?.let(extension::setReportsDir)
    }
}
