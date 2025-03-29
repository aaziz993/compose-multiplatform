package gradle.plugins.java

import gradle.accessors.catalog.libs
import gradle.accessors.java
import gradle.accessors.settings
import gradle.api.applyTo
import gradle.api.tasks.SourceSet
import gradle.api.tryApplyAction
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.ifTrue
import gradle.plugins.java.manifest.Manifest
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion
import org.gradle.api.Project

/**
 * Common configuration for JVM (Java) based projects.
 *
 * This extension is added by the [JavaBasePlugin] and would be more appropriately named
 * the `JvmPluginExtension` extension.  It is used to configure many of the project's
 * JVM-related settings and behavior.
 *
 * @since 4.10
 */
@Serializable
internal data class JavaPluginExtension(
    /**
     * Sets the source compatibility used for compiling Java sources.
     *
     *
     * This property cannot be set if a [toolchain][.getToolchain] has been configured.
     *
     * @param value The value for the source compatibility
     *
     * @see .toolchain
     */
    val sourceCompatibility: JavaVersion? = null,
    /**
     * Sets the target compatibility used for compiling Java sources.
     *
     *
     * This property cannot be set if a [toolchain][.getToolchain] has been configured.
     *
     * @param value The value for the target compatibility
     *
     * @see .toolchain
     */
    val targetCompatibility: JavaVersion? = null,
    /**
     * If this method is called, Gradle will not automatically try to fetch
     * dependencies which have a JVM version compatible with the target compatibility
     * of this module.
     * <P>
     * This should be used whenever the default behavior is not
     * applicable, in particular when for some reason it's not possible to split
     * a module and that this module only has some classes which require dependencies
     * on higher versions.
     *
     * @since 5.3
    </P> */
    val disableAutoTargetJvm: Boolean? = null,
    /**
     * Adds a task `javadocJar` that will package the output of the `javadoc` task in a JAR with classifier `javadoc`.
     * <P>
     * The produced artifact is registered as a documentation variant on the `java` component and added as a dependency on the `assemble` task.
     * This means that if `maven-publish` or `ivy-publish` is also applied, the javadoc JAR will be published.
    </P> * <P>
     * If the project already has a task named `javadocJar` then no task is created.
    </P> * <P>
     * The publishing of the Javadoc variant can also be disabled using [org.gradle.api.component.ConfigurationVariantDetails.skip]
     * through [org.gradle.api.component.AdhocComponentWithVariants.withVariantsFromConfiguration],
     * if it should only be built locally by calling or wiring the ':javadocJar' task.
     *
     * @since 6.0
    </P> */
    val withJavadocJar: Boolean? = null,
    /**
     * Adds a task `sourcesJar` that will package the Java sources of the main [SourceSet][org.gradle.api.tasks.SourceSet] in a JAR with classifier `sources`.
     * <P>
     * The produced artifact is registered as a documentation variant on the `java` component and added as a dependency on the `assemble` task.
     * This means that if `maven-publish` or `ivy-publish` is also applied, the sources JAR will be published.
    </P> * <P>
     * If the project already has a task named `sourcesJar` then no task is created.
    </P> * <P>
     * The publishing of the sources variant can be disabled using [org.gradle.api.component.ConfigurationVariantDetails.skip]
     * through [org.gradle.api.component.AdhocComponentWithVariants.withVariantsFromConfiguration],
     * if it should only be built locally by calling or wiring the ':sourcesJar' task.
     *
     * @since 6.0
    </P> */
    val withSourcesJar: Boolean? = null,
    /**
     * Configure the module path handling for tasks that have a 'classpath' as input. The module classpath handling defines
     * to determine for each entry if it is passed to Java tools using '-classpath' or '--module-path'.
     *
     * @since 6.4
     */
    val modularity: ModularitySpec? = null,
    /**
     * Gets the project wide toolchain requirements that will be used for tasks requiring a tool from the toolchain (e.g. [org.gradle.api.tasks.compile.JavaCompile]).
     *
     *
     * Configuring a toolchain cannot be used together with `sourceCompatibility` or `targetCompatibility` on this extension.
     * Both values will be sourced from the toolchain.
     *
     * @since 6.7
     */
    val toolchain: JavaToolchainSpec? = null,
    /**
     * Configure the dependency resolution consistency for this Java project.
     *
     * @param action the configuration action
     *
     * @since 6.8
     */
    val consistentResolution: JavaResolutionConsistency? = null,
    val sourceSets: LinkedHashSet<SourceSet>? = null,
    /**
     * Sets a file pointing to the root directory supposed to be used for all docs.
     * @since 7.1
     */
    val docsDir: String? = null,
    /**
     * Sets a file pointing to the root directory of the test results.
     * @since 7.1
     */
    val testResultsDir: String? = null,
    /**
     * Sets a file pointing to the root directory to be used for reports.
     * @since 7.1
     */
    val testReportDir: String? = null,
    /**
     * Creates a new instance of a [gradle.plugins.java.manifest.Manifest].
     * @since 7.1
     */
    val manifest: Manifest? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo() =
        project.pluginManager.withPlugin("java") {
            project.java::setSourceCompatibility trySet (sourceCompatibility
                ?: project.settings.libs.versionOrNull("java.sourceCompatibility")
                    ?.let(JavaVersion::toVersion))
            project.java::setTargetCompatibility trySet (targetCompatibility
                ?: project.settings.libs.versionOrNull("java.targetCompatibility")
                    ?.let(JavaVersion::toVersion))
            disableAutoTargetJvm?.ifTrue(project.java::disableAutoTargetJvm)
            withJavadocJar?.ifTrue(project.java::withJavadocJar)
            withSourcesJar?.ifTrue(project.java::withSourcesJar)
            modularity?.applyTo(project.java.modularity)
            toolchain?.applyTo(project.java.toolchain)
            project.java::consistentResolution tryApplyAction consistentResolution?.let { consistentResolution -> consistentResolution::applyTo }

            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.java.sourceSets)
            }

            project.java.docsDir tryAssign docsDir?.let(project.layout.projectDirectory::dir)
            project.java.testResultsDir tryAssign testResultsDir?.let(project.layout.projectDirectory::dir)
            project.java.testReportDir tryAssign testReportDir?.let(project.layout.projectDirectory::dir)
            manifest?.applyTo(project.java.manifest())
        }
}
