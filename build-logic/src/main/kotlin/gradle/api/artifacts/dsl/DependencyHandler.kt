@file:Suppress(
    "unused",
    "nothing_to_inline",
    "useless_cast",
    "unchecked_cast",
    "extension_shadowed_by_member",
    "redundant_projection",
    "RemoveRedundantBackticks",
    "ObjectPropertyName",
    "deprecation",
)
@file:org.gradle.api.Generated

/* ktlint-disable */

package gradle.api.artifacts.dsl

import org.gradle.api.Action
import org.gradle.api.artifacts.*
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.artifacts.dsl.DependencyConstraintHandler
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible
import org.gradle.kotlin.dsl.accessors.runtime.addConfiguredDependencyTo
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.create

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.debugImplementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "debugImplementation", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.debugImplementation(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "debugImplementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.debugImplementation(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "debugImplementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.debugImplementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "debugImplementation", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.debugImplementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("debugImplementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'debugImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.debugImplementation(constraintNotation: Any): DependencyConstraint =
    add("debugImplementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'debugImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.debugImplementation(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("debugImplementation", constraintNotation, block)

/**
 * Adds an artifact to the 'debugImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.debugImplementation(artifactNotation: Any): PublishArtifact =
    add("debugImplementation", artifactNotation)

/**
 * Adds an artifact to the 'debugImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.debugImplementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("debugImplementation", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.implementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "implementation", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.implementation(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "implementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.implementation(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "implementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.implementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "implementation", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.implementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("implementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.implementation(constraintNotation: Any): DependencyConstraint =
    add("implementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.implementation(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("implementation", constraintNotation, block)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.implementation(artifactNotation: Any): PublishArtifact =
    add("implementation", artifactNotation)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.implementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("implementation", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.api(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "api", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.api(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "api", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.api(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "api", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.api(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "api", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.api(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("api", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'api' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.api(constraintNotation: Any): DependencyConstraint =
    add("api", constraintNotation)

/**
 * Adds a dependency constraint to the 'api' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.api(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("api", constraintNotation, block)

/**
 * Adds an artifact to the 'api' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.api(artifactNotation: Any): PublishArtifact =
    add("api", artifactNotation)

/**
 * Adds an artifact to the 'api' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.api(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("api", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testImplementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "testImplementation", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testImplementation(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testImplementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testImplementation(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testImplementation", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testImplementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "testImplementation", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.testImplementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("testImplementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'testImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testImplementation(constraintNotation: Any): DependencyConstraint =
    add("testImplementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'testImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testImplementation(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("testImplementation", constraintNotation, block)

/**
 * Adds an artifact to the 'testImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testImplementation(artifactNotation: Any): PublishArtifact =
    add("testImplementation", artifactNotation)

/**
 * Adds an artifact to the 'testImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testImplementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("testImplementation", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.compileOnly(dependencyNotation: Any): Dependency? =
    add("compileOnly", dependencyNotation)

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.compileOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "compileOnly", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.compileOnly(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "compileOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.compileOnly(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "compileOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.compileOnly(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "compileOnly", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'compileOnly' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.compileOnly(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("compileOnly", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'compileOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.compileOnly(constraintNotation: Any): DependencyConstraint =
    add("compileOnly", constraintNotation)

/**
 * Adds a dependency constraint to the 'compileOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.compileOnly(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("compileOnly", constraintNotation, block)

/**
 * Adds an artifact to the 'compileOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.compileOnly(artifactNotation: Any): PublishArtifact =
    add("compileOnly", artifactNotation)

/**
 * Adds an artifact to the 'compileOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.compileOnly(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("compileOnly", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testCompileOnly(dependencyNotation: Any): Dependency? =
    add("testCompileOnly", dependencyNotation)

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testCompileOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "testCompileOnly", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testCompileOnly(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testCompileOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testCompileOnly(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testCompileOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testCompileOnly(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "testCompileOnly", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testCompileOnly' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.testCompileOnly(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("testCompileOnly", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'testCompileOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testCompileOnly(constraintNotation: Any): DependencyConstraint =
    add("testCompileOnly", constraintNotation)

/**
 * Adds a dependency constraint to the 'testCompileOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testCompileOnly(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("testCompileOnly", constraintNotation, block)

/**
 * Adds an artifact to the 'testCompileOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testCompileOnly(artifactNotation: Any): PublishArtifact =
    add("testCompileOnly", artifactNotation)

/**
 * Adds an artifact to the 'testCompileOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testCompileOnly(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("testCompileOnly", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.runtimeOnly(dependencyNotation: Any): Dependency? =
    add("runtimeOnly", dependencyNotation)

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.runtimeOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "runtimeOnly", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.runtimeOnly(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "runtimeOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.runtimeOnly(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "runtimeOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.runtimeOnly(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "runtimeOnly", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'runtimeOnly' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.runtimeOnly(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("runtimeOnly", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'runtimeOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.runtimeOnly(constraintNotation: Any): DependencyConstraint =
    add("runtimeOnly", constraintNotation)

/**
 * Adds a dependency constraint to the 'runtimeOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.runtimeOnly(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("runtimeOnly", constraintNotation, block)

/**
 * Adds an artifact to the 'runtimeOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.runtimeOnly(artifactNotation: Any): PublishArtifact =
    add("runtimeOnly", artifactNotation)

/**
 * Adds an artifact to the 'runtimeOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.runtimeOnly(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("runtimeOnly", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
    add("testRuntimeOnly", dependencyNotation)

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testRuntimeOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "testRuntimeOnly", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testRuntimeOnly(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testRuntimeOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testRuntimeOnly(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testRuntimeOnly", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testRuntimeOnly(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "testRuntimeOnly", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testRuntimeOnly' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.testRuntimeOnly(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("testRuntimeOnly", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'testRuntimeOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testRuntimeOnly(constraintNotation: Any): DependencyConstraint =
    add("testRuntimeOnly", constraintNotation)

/**
 * Adds a dependency constraint to the 'testRuntimeOnly' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testRuntimeOnly(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("testRuntimeOnly", constraintNotation, block)

/**
 * Adds an artifact to the 'testRuntimeOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testRuntimeOnly(artifactNotation: Any): PublishArtifact =
    add("testRuntimeOnly", artifactNotation)

/**
 * Adds an artifact to the 'testRuntimeOnly' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testRuntimeOnly(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("testRuntimeOnly", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.annotationProcessor(dependencyNotation: Any): Dependency? =
    add("annotationProcessor", dependencyNotation)

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.annotationProcessor(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "annotationProcessor", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.annotationProcessor(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "annotationProcessor", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.annotationProcessor(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "annotationProcessor", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.annotationProcessor(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "annotationProcessor", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'annotationProcessor' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.annotationProcessor(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("annotationProcessor", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'annotationProcessor' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.annotationProcessor(constraintNotation: Any): DependencyConstraint =
    add("annotationProcessor", constraintNotation)

/**
 * Adds a dependency constraint to the 'annotationProcessor' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.annotationProcessor(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("annotationProcessor", constraintNotation, block)

/**
 * Adds an artifact to the 'annotationProcessor' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.annotationProcessor(artifactNotation: Any): PublishArtifact =
    add("annotationProcessor", artifactNotation)

/**
 * Adds an artifact to the 'annotationProcessor' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.annotationProcessor(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("annotationProcessor", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testAnnotationProcessor(dependencyNotation: Any): Dependency? =
    add("testAnnotationProcessor", dependencyNotation)

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testAnnotationProcessor(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "testAnnotationProcessor", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testAnnotationProcessor(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testAnnotationProcessor", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testAnnotationProcessor(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "testAnnotationProcessor", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.testAnnotationProcessor(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "testAnnotationProcessor", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'testAnnotationProcessor' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.testAnnotationProcessor(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("testAnnotationProcessor", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'testAnnotationProcessor' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testAnnotationProcessor(constraintNotation: Any): DependencyConstraint =
    add("testAnnotationProcessor", constraintNotation)

/**
 * Adds a dependency constraint to the 'testAnnotationProcessor' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.testAnnotationProcessor(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("testAnnotationProcessor", constraintNotation, block)

/**
 * Adds an artifact to the 'testAnnotationProcessor' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testAnnotationProcessor(artifactNotation: Any): PublishArtifact =
    add("testAnnotationProcessor", artifactNotation)

/**
 * Adds an artifact to the 'testAnnotationProcessor' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.testAnnotationProcessor(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("testAnnotationProcessor", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokka(dependencyNotation: Any): Dependency? =
    add("dokka", dependencyNotation)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokka(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "dokka", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokka(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "dokka", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokka(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "dokka", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokka(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "dokka", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.dokka(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("dokka", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.dokka(constraintNotation: Any): DependencyConstraint =
    add("dokka", constraintNotation)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.dokka(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("dokka", constraintNotation, block)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.dokka(artifactNotation: Any): PublishArtifact =
    add("dokka", artifactNotation)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.dokka(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("dokka", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokkaPlugin(dependencyNotation: Any): Dependency? =
    add("dokkaPlugin", dependencyNotation)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokkaPlugin(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "dokkaPlugin", dependencyNotation, dependencyConfiguration,
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokkaPlugin(
    dependencyNotation: Provider<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "dokkaPlugin", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokkaPlugin(
    dependencyNotation: ProviderConvertible<*>,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Unit = addConfiguredDependencyTo(
    this, "dokkaPlugin", dependencyNotation, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
public fun DependencyHandler.dokkaPlugin(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this, "dokkaPlugin", group, name, version, configuration, classifier, ext, dependencyConfiguration,
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
public fun <T : ModuleDependency> DependencyHandler.dokkaPlugin(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("dokkaPlugin", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.dokkaPlugin(constraintNotation: Any): DependencyConstraint =
    add("dokkaPlugin", constraintNotation)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
public fun DependencyConstraintHandler.dokkaPlugin(constraintNotation: Any, block: DependencyConstraint.() -> Unit): DependencyConstraint =
    add("dokkaPlugin", constraintNotation, block)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.dokkaPlugin(artifactNotation: Any): PublishArtifact =
    add("dokkaPlugin", artifactNotation)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
public fun ArtifactHandler.dokkaPlugin(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("dokkaPlugin", artifactNotation, configureAction)