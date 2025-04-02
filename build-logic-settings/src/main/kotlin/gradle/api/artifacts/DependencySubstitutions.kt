package gradle.api.artifacts

import gradle.get
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.artifacts.VariantSelectionDetails
import org.gradle.api.artifacts.component.ComponentSelector

/**
 * Allows replacing dependencies with other dependencies.
 *
 * @since 2.5
 */
@Serializable
internal data class DependencySubstitutions(
    /**
     * Adds a dependency substitution rule that is triggered for every dependency (including transitive)
     * when the configuration is being resolved. The action receives an instance of [DependencySubstitution]
     * that can be used to find out what dependency is being resolved and to influence the resolution process.
     *
     *
     * Example:
     * <pre class='autoTested'>
     * configurations { main }
     * // add dependency substitution rules
     * configurations.main.resolutionStrategy.dependencySubstitution {
     * // Use a rule to change the dependency module while leaving group + version intact
     * all { DependencySubstitution dependency -&gt;
     * if (dependency.requested instanceof ModuleComponentSelector &amp;&amp; dependency.requested.module == 'groovy-all') {
     * dependency.useTarget dependency.requested.group + ':groovy:' + dependency.requested.version
     * }
     * }
     * // Use a rule to replace all missing projects with module dependencies
     * all { DependencySubstitution dependency -&gt;
     * if (dependency.requested instanceof ProjectComponentSelector) {
     * def targetProject = findProject(":${dependency.requested.path}")
     * if (targetProject == null) {
     * dependency.useTarget "org.myorg:" + dependency.requested.path + ":+"
     * }
     * }
     * }
     * }
    </pre> *
     *
     * The rules are evaluated in order they are declared. Rules are evaluated after forced modules are applied (see [ResolutionStrategy.force]
     *
     * @return this
     */
    val all: DependencySubstitution? = null,
    /**
     * Transforms the supplied selector into a specific variant selector.
     *
     * @param selector the origin selector
     * @param detailsAction the variant selection details configuration
     * @since 6.6
     */
//val  variant(
//    selector: ComponentSelector?,
//    detailsAction:  VariantSelectionDetails?>?
//): ComponentSelector?

    /**
     * DSL-friendly mechanism to construct a dependency substitution for dependencies matching the provided selector.
     *
     *
     * Examples:
     * <pre class='autoTested'>
     * configurations { main }
     * configurations.main.resolutionStrategy.dependencySubstitution {
     * // Substitute project and module dependencies
     * substitute module('org.gradle:api') using project(':api')
     * substitute project(':util') using module('org.gradle:util:3.0')
     *
     * // Substitute one module dependency for another
     * substitute module('org.gradle:api:2.0') using module('org.gradle:api:2.1')
     * }
    </pre> *
     */
    val substitutes: Set<Substitute>? = null,
) {

    context(Project)
    fun applyTo(receiver: DependencySubstitutions) {
        all?.let { all ->
            receiver.all {
                all.applyTo(this)
            }
        }


        substitutes?.forEach { substitute ->
            receiver.substitute(
                receiver[substitute.componentSelector],
            ).apply {
                substitute.substitution?.applyTo(this, receiver::get)
            }
        }
    }

    /**
     * Provides a DSL-friendly mechanism for specifying the target of a substitution.
     */
    @Serializable
    data class Substitution(
        /**
         * Specify a reason for the substitution. This is optional
         *
         * @param reason the reason for the selection
         * @return the substitution
         * @since 4.5
         */
        val because: String? = null,
        /**
         * Specifies that the substituted target dependency should use the specified classifier.
         *
         * This method assumes that the target dependency is a jar (type jar, extension jar).
         *
         * @since 6.6
         */
        val withClassifier: String? = null,
        /**
         * Specifies that the substituted dependency mustn't have any classifier.
         * It can be used whenever you need to substitute a dependency which uses a classifier into
         * a dependency which doesn't.
         *
         * This method assumes that the target dependency is a jar (type jar, extension jar).
         *
         * @since 6.6
         */
        val withoutClassifier: Boolean? = null,
        /**
         * Specifies that substituted dependencies must not carry any artifact selector.
         *
         * @since 6.6
         */
        val withoutArtifactSelectors: Boolean? = null,
        /**
         * Specify the target of the substitution. This is a replacement for the prior `#with(ComponentSelector)`
         * method which supports chaining.
         *
         * @since 6.6
         */
        val using: String? = null,
    ) {

        context(Project)
        fun applyTo(receiver: DependencySubstitutions.Substitution, getComponentSelector: (componentSelector: String) -> ComponentSelector) {
            receiver::because trySet because
            receiver::withClassifier trySet withClassifier
            receiver::withoutClassifier trySet withoutClassifier
            receiver::withoutArtifactSelectors trySet withoutArtifactSelectors
            receiver::using trySet using?.let(getComponentSelector)
        }
    }
}

private operator fun DependencySubstitutions.get(componentSelector: String): ComponentSelector =
    get(*componentSelector.removePrefix("$").split(".").toTypedArray()) as ComponentSelector

