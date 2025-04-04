package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.DependencySubstitution
import org.gradle.api.initialization.Settings

/**
 * Provides means to substitute a different dependency during resolution.
 *
 * @since 2.5
 */
@Serializable
internal data class DependencySubstitution(
    /**
     * This method can be used to replace a dependency before it is resolved,
     * e.g. change group, name or version (or all three of them), or replace it
     * with a project dependency and provides a human readable reason for diagnostics.
     *
     * Accepted notations are:
     *
     *  * Strings encoding group:module:version, like 'org.gradle:gradle-core:2.4'
     *  * Maps like [group: 'org.gradle', name: 'gradle-core', version: '2.4']
     *  * Project instances like `project(":api")`
     *  * Any instance of `ModuleComponentSelector` or `ProjectComponentSelector`
     *
     *
     * @param notation the notation that gets parsed into an instance of [ComponentSelector].
     *
     * @since 4.5
     */
    val useTarget: Target? = null,
    /**
     * Configures the artifact selection for the substitution.
     * This is a convenience method which allows selecting, typically, different artifact classifiers
     * for the same component.
     *
     * Artifact selection matters for components which are not published with Gradle Module Metadata
     * and therefore do not provide proper variants to reason with.
     *
     * @param action the artifact selection configuration action
     *
     * @since 6.6
     */
    val artifactSelection: ArtifactSelectionDetails? = null,
) {

    context(Settings)
    fun applyTo(receiver: DependencySubstitution) {
        useTarget?.let { (notation, reason) ->
            val resolved= allLibs.resolveDependency(notation,set)
            reason?.let { reason -> receiver.useTarget(notation, reason) }
                ?: receiver.useTarget(notation) // TODO notation resolver
        }

        artifactSelection?.let { artifactSelection ->
            receiver.artifactSelection(artifactSelection::applyTo)
        }
    }
}
