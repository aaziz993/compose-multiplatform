package gradle.api.initialization

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.initialization.ConfigurableIncludedBuild

/**
 * A build that is to be included in the composite.
 *
 * @since 3.1
 */
@Serializable
internal data class ConfigurableIncludedBuild(
    /**
     * Sets the name of the included build.
     *
     * @param name the name of the build
     * @since 6.0
     */
    override val name: String? = null,
    /**
     * Configures the dependency substitution rules for this included build.
     *
     * The action receives an instance of [DependencySubstitutions] which can be configured with substitution rules.
     * Project dependencies are resolved in the context of the included build.
     *
     * @see org.gradle.api.artifacts.ResolutionStrategy.dependencySubstitution
     * @see DependencySubstitutions
     */
    val dependencySubstitution: DependencySubstitutions?
) : IncludedBuild<ConfigurableIncludedBuild> {

    override fun applyTo(receiver: ConfigurableIncludedBuild) {
        receiver::setName trySet name
    }
}
