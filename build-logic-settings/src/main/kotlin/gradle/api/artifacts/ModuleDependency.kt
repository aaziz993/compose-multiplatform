package gradle.api.artifacts

import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.exclude

/**
 * A `ModuleDependency` is a [org.gradle.api.artifacts.Dependency] on a component that exists
 * outside of the current project.
 *
 *
 * Modules can supply [multiple artifacts][ModuleDependency.getArtifacts] in addition to the
 * [implicit default artifact][.addArtifact].  Non-default artifacts
 * available in a module can be selected by a consumer by specifying a classifier or extension
 * when declaring a dependency on that module.
 *
 *
 * For examples on configuring exclude rules for modules please refer to [.exclude].
 */
internal interface ModuleDependency<T : org.gradle.api.artifacts.ModuleDependency> : Dependency<T> {

    /**
     * Adds an exclude rule to exclude transitive dependencies of this dependency.
     *
     *
     * Excluding a particular transitive dependency does not guarantee that it does not show up
     * in the dependencies of a given configuration.
     * For example, some other dependency, which does not have any exclude rules,
     * might pull in exactly the same transitive dependency.
     * To guarantee that the transitive dependency is excluded from the entire configuration
     * please use per-configuration exclude rules: [Configuration.getExcludeRules].
     * In fact, in a majority of cases the actual intention of configuring per-dependency exclusions
     * is really excluding a dependency from the entire configuration (or classpath).
     *
     *
     * If your intention is to exclude a particular transitive dependency
     * because you don't like the version it pulls in to the configuration
     * then consider using forced versions' feature: [ResolutionStrategy.force].
     *
     * <pre class='autoTested'>
     * plugins {
     * id 'java' // so that I can declare 'implementation' dependencies
     * }
     *
     * dependencies {
     * implementation('org.hibernate:hibernate:3.1') {
     * //excluding a particular transitive dependency:
     * exclude module: 'cglib' //by artifact name
     * exclude group: 'org.jmock' //by group
     * exclude group: 'org.unwanted', module: 'iAmBuggy' //by both name and group
     * }
     * }
    </pre> *
     *
     * @param excludeProperties the properties to define the exclude rule.
     * @return this
     */
    val excludeRules: Set<ExcludeRule>?

    /**
     * Returns the artifacts belonging to this dependency.
     *
     *
     * Initially, a dependency has no artifacts, so this can return an empty set.  Typically, however, a producer
     * project will add a single artifact to a module, which will be represented in this collection via a
     * single element.  But this is **NOT** always true.  Modules can use
     * custom classifiers or extensions to distinguish multiple artifacts that they contain.
     *
     *
     * In general, projects publishing using Gradle should favor supplying multiple artifacts by supplying
     * multiple variants, each containing a different artifact, that are selectable through variant-aware
     * dependency resolution.  This mechanism where a module contains multiple artifacts is primarily
     * intended to support dependencies on non-Gradle-published components.
     *
     * @see .addArtifact
     */
    val artifacts: Set<DependencyArtifact>?

    /**
     * Sets whether this dependency should be resolved including or excluding its transitive dependencies. The artifacts
     * belonging to this dependency might themselves have dependencies on other artifacts. The latter are called
     * transitive dependencies.
     *
     * @param transitive Whether transitive dependencies should be resolved.
     * @return this
     */
    val transitive: Boolean?

    /**
     * Sets the requested target configuration of this dependency.
     *
     *
     * This overrides variant-aware dependency resolution and selects the variant in the
     * target component matching the requested configuration name.
     *
     *
     * Using this method is **discouraged** except for selecting
     * configurations from Ivy components.
     *
     * @since 4.0
     */
    val targetConfiguration: String?

    /**
     * {@inheritDoc}
     */

    /**
     * Configures the requested capabilities of this dependency.
     *
     * @param configureAction the configuration action
     * @return this
     *
     * @since 5.3
     */
    val capabilities: ModuleDependencyCapabilitiesHandler?

    /**
     * Endorse version constraints with [VersionConstraint.getStrictVersion] strict versions} from the target module.
     *
     * Endorsing strict versions of another module/platform means that all strict versions will be interpreted during dependency
     * resolution as if they were defined by the endorsing module itself.
     *
     * @since 6.0
     */
    val endorseStrictVersions: Boolean?

    /**
     * Resets the [.isEndorsingStrictVersions] state of this dependency.
     *
     * @since 6.0
     */
    val doNotEndorseStrictVersions: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        excludeRules?.forEach { (group, module) ->
            receiver.exclude(group, module)
        }

        artifacts?.forEach { artifact ->
            receiver.artifact(artifact::applyTo)
        }

        receiver::setTransitive trySet transitive
        receiver::setTargetConfiguration trySet targetConfiguration

        capabilities?.let { capabilities ->
            receiver.capabilities {
                capabilities.applyTo(this)
            }
        }

        receiver::endorseStrictVersions trySet endorseStrictVersions
        receiver::doNotEndorseStrictVersions trySet doNotEndorseStrictVersions
    }
}
