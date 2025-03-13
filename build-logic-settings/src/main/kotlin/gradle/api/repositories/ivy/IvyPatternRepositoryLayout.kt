package gradle.api.repositories.ivy

import gradle.api.repositories.RepositoryLayout
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.repositories.IvyPatternRepositoryLayout

/**
 * A repository layout that uses user-supplied patterns. Each pattern will be appended to the base URI for the repository.
 * At least one artifact pattern must be specified. If no Ivy patterns are specified, then the artifact patterns will be used.
 * Optionally supports a Maven style layout for the 'organisation' part, replacing any dots with forward slashes.
 *
 * For examples see the reference for [org.gradle.api.artifacts.repositories.IvyArtifactRepository.patternLayout].
 *
 * @since 2.3 (feature was already present in Groovy DSL, this type introduced in 2.3)
 */
@Serializable
internal data class IvyPatternRepositoryLayout(
    /**
     * Adds an Ivy artifact pattern to define where artifacts are located in this repository.
     * @param pattern The ivy pattern
     */
    val artifacts: List<String>? = null,
    /**
     * Adds an Ivy pattern to define where ivy files are located in this repository.
     * @param pattern The ivy pattern
     */
    val ivys: List<String>? = null,
    /**
     * Sets whether a Maven style layout is to be used for the 'organisation' part, replacing any dots with forward slashes.
     * Defaults to `false`.
     *
     * @param m2compatible whether a Maven style layout is to be used for the 'organisation' part
     */
    val m2compatible: Boolean? = null,
) : RepositoryLayout {

    override fun applyTo(layout: org.gradle.api.artifacts.repositories.RepositoryLayout) {
        layout as IvyPatternRepositoryLayout

        artifacts?.forEach(layout::artifact)
        ivys?.forEach(layout::ivy)
        m2compatible?.let(layout::setM2compatible)
    }
}
