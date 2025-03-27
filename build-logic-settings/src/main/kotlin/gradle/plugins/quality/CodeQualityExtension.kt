package gradle.plugins.quality

import gradle.accessors.sourceSets
import gradle.api.getByNameOrAll
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeQualityExtension

/**
 * Base Code Quality Extension.
 */
internal abstract class CodeQualityExtension<T : CodeQualityExtension> {

    /**
     * The version of the code quality tool to be used.
     */
    abstract val toolVersion: String?

    /**
     * The source sets to be analyzed as part of the `check` and `build` tasks.
     */
    abstract val sourceSets: Set<String>?

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
    fun applyTo(receiver: T) {
        receiver::setToolVersion trySet toolVersion
        sourceSets?.flatMap(project.sourceSets::getByNameOrAll)?.let(receiver::setSourceSets)
        receiver::setIgnoreFailures trySet ignoreFailures
        reportsDir?.let(project::file)?.let(receiver::setReportsDir)
    }
}
