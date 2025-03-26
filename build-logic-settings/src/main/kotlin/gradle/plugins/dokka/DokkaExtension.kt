package gradle.plugins.dokka

import gradle.accessors.dokka
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.plugins.dokka.plugin.DokkaPluginParametersBaseSpec
import gradle.plugins.dokka.plugin.DokkaPluginParametersBaseSpecTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.workers.ClassLoaderIsolation

/**
 * Configure the behaviour of the [DokkaBasePlugin].
 */
internal interface DokkaExtension {

    /**
     * Base directory into which all [DokkaPublication]s will be produced.
     * By default, Dokka will generate all [DokkaPublication]s into a subdirectory inside [basePublicationsDirectory].
     *
     * To configure the output for a specific Publication, instead use [DokkaPublication.outputDirectory].
     *
     * #### Example
     *
     * Here we configure the output directory to be `./build/dokka-docs/`.
     * Dokka will produce the HTML Publication into `./build/dokka-docs/html/`
     * ```
     * dokka {
     *     basePublicationsDirectory.set(layout.buildDirectory.dir("dokka-docs"))
     * }
     * ```
     */
    val basePublicationsDirectory: String?

    /** Default Dokka Gradle Plugin cache directory */
    val dokkaCacheDirectory: String?

    /**
     * The display name used to refer to the module.
     * It is used for the table of contents, navigation, logging, etc.
     *
     * Default: the [current project name][Project.name].
     */
    val moduleName: String?

    /**
     * The displayed module version.
     *
     * Default: the [version of the current project][Project.version].
     */
    val moduleVersion: String?

    /**
     * Control the subdirectory used for files when aggregating this project as a Dokka Module into a Dokka Publication.
     *
     * When Dokka performs aggregation the files from each Module must be placed into separate
     * subdirectories, within the Publication directory.
     * The subdirectory used for this project's Module can be specified with this property.
     *
     * Overriding this value can be useful for fine-grained control.
     * - Setting an explicit path can help ensure that external hyperlinks to the Publication are stable,
     *   regardless of how the current Gradle project is structured.
     * - The path can also be made more specific, which is useful for
     *   [Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html),
     *   which can be more likely to cause path clashes.
     *   (The default value is distinct for a single Gradle build. With composite builds the project paths may not be distinct.)
     *   See the [Composite Build Example](https://kotl.in/dokka/examples/gradle-composite-build).
     *
     * **Important:** Care must be taken to make sure multiple Dokka Modules do not have the same paths.
     * If paths overlap then Dokka could overwrite the Modules files during aggregation,
     * resulting in a corrupted Publication.
     *
     * Default: the current project's [path][Project.getPath] as a file path.
     */
    val modulePath: String?

    /**
     * An arbitrary string used to group source sets that originate from different Gradle subprojects.
     *
     * This is primarily used by Kotlin Multiplatform projects, which can have multiple source sets
     * per subproject.
     *
     * Defaults to [the Gradle path of the subproject][Project.getPath].
     */
    val sourceSetScopeDefault: String?

    /**
     * The Konan home directory, which contains libraries for Kotlin/Native development.
     *
     * This is only required as a workaround to fetch the compile-time dependencies in Kotlin/Native
     * projects with a version below 2.0.
     */
    // This property should be removed when Dokka only supports KGP 2 or higher.
    val konanHome: String?

    /**
     * The container for all [DokkaPublication]s in the current project.
     *
     * Each Dokka Publication will generate one complete Dokka site,
     * aggregated from one or more Dokka Modules.
     *
     * The type of site is determined by the Dokka Plugins. By default, an HTML site will be generated.
     *
     * #### Configuration
     *
     * To configure a specific Dokka Publications, select it by name:
     *
     * ```
     * dokka {
     *   dokkaPublications.named("html") {
     *     // ...
     *   }
     * }
     * ```
     *
     * All configurations can be configured using `.configureEach {}`:
     *
     * ```
     * dokka {
     *   dokkaPublications.configureEach {
     *     // ...
     *   }
     * }
     * ```
     */
    val dokkaPublications: LinkedHashSet<DokkaPublication>?

    /**
     * The container for all [DokkaSourceSet][DokkaSourceSetSpec]s in the current project.
     *
     * Each `DokkaSourceSet` is analogous to a [SourceSet][org.gradle.api.tasks.SourceSet],
     * and specifies how Dokka will convert the project's source code into documentation.
     *
     * Dokka will automatically discover the current source sets in the project and create
     * a `DokkaSourceSet` for each. For example, in a Kotlin Multiplatform project Dokka
     * will create `DokkaSourceSet`s for `commonMain`, `jvmMain` etc.
     *
     * Dokka will not generate documentation unless there is at least one Dokka Source Set.
     *
     * #### Configuration
     *
     * To configure a specific Dokka Source Set, select it by name:
     *
     * ```
     * dokka {
     *   dokkaSourceSets.named("commonMain") {
     *     // ...
     *   }
     * }
     * ```
     *
     * All Source Sets can be configured using `.configureEach {}`:
     *
     * ```
     * dokka {
     *   dokkaSourceSets.configureEach {
     *     // ...
     *   }
     * }
     * ```
     */
    val dokkaSourceSets: LinkedHashSet<DokkaSourceSetSpec>?

    /**
     * Dokka Plugin are used to configure the way Dokka generates a format.
     * Some plugins can be configured via parameters, and those parameters are stored in this
     * container.
     */
    val pluginsConfiguration: LinkedHashSet<@Serializable(with = DokkaPluginParametersBaseSpecTransformingSerializer::class) DokkaPluginParametersBaseSpec<out org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec>>?

    /**
     * The default version of Dokka dependencies that are used at runtime during generation.
     *
     * This value defaults to the current Dokka Gradle Plugin version, but can be overridden
     * if you want to use a newer or older version of Dokka at runtime.
     */
    val dokkaEngineVersion: String?

    /**
     * Dokka Gradle Plugin runs Dokka Generator in a separate
     * [Gradle Worker](https://docs.gradle.org/8.10/userguide/worker_api.html).
     *
     * DGP uses a Worker to ensure that the Java classpath required by Dokka Generator
     * is kept separate from the Gradle buildscript classpath, ensuring that dependencies
     * required for running Gradle builds don't interfere with those needed to run Dokka.
     *
     * #### Worker modes
     *
     * DGP can launch the Generator in one of two Worker modes.
     *
     * The Worker modes are used to optimise the performance of a Gradle build,
     * especially concerning the memory requirements.
     *
     * ##### [ProcessIsolation]
     *
     * The maximum isolation level. Dokka Generator is executed in a separate Java process,
     * managed by Gradle.
     *
     * The Java process parameters (such as JVM args and system properties) can be configured precisely,
     * and independently of other Gradle processes.
     *
     * Process isolation is best suited for projects where Dokka requires a lot more, or less,
     * memory than other Gradle tasks that are run more frequently.
     * This is usually the case for smaller projects, or those with default or low
     * [Gradle Daemon](https://docs.gradle.org/8.10/userguide/gradle_daemon.html)
     * memory settings.
     *
     * ##### [ClassLoaderIsolation]
     *
     * Dokka Generator is run in the current Gradle Daemon process, in a new thread with an isolated classpath.
     *
     * Classloader isolation is best suited for projects that already have high Gradle Daemon memory requirements.
     * This is usually the case for very large projects, especially Kotlin Multiplatform projects.
     * These projects will typically also require a lot of memory to running Dokka Generator.
     *
     * If the Gradle Daemon already uses a large amount of memory, it is beneficial to run Dokka Generator
     * in the same Daemon process. Running Dokka Generator inside the Daemon avoids launching
     * two Java processes on the same machine, both with high memory requirements.
     *
     * #### Example configuration
     *
     * ```kotlin
     * dokka {
     *   // use the current Gradle process, but with an isolated classpath
     *   dokkaGeneratorIsolation = ClassLoaderIsolation()
     *
     *   // launch a new process, optionally controlling the standard JVM options
     *   dokkaGeneratorIsolation = ProcessIsolation {
     *     maxHeapSize = "2g" // increase maximum heap size
     *     systemProperties.add("someCustomProperty", 123)
     *   }
     * }
     * ```
     *
     * @see WorkerIsolation
     * @see org.jetbrains.dokka.gradle.workers.ProcessIsolation
     * @see ClassLoaderIsolation
     */
    // Aside: Launching without isolation WorkerExecutor.noIsolation is not an option, because
    // running Dokka Generator **requires** an isolated classpath.
    val dokkaGeneratorIsolation: WorkerIsolation?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("dokka").id) {
            project.dokka.basePublicationsDirectory tryAssign basePublicationsDirectory?.let(project.layout.projectDirectory::dir)
            project.dokka.dokkaCacheDirectory tryAssign dokkaCacheDirectory?.let(project.layout.projectDirectory::dir)
            project.dokka.moduleName tryAssign moduleName
            project.dokka.moduleVersion tryAssign moduleVersion
            project.dokka.modulePath tryAssign modulePath
            project.dokka.sourceSetScopeDefault tryAssign sourceSetScopeDefault
            project.dokka.konanHome tryAssign konanHome?.let(project::file)

            dokkaPublications?.forEach { dokkaPublication ->
                dokkaPublication.applyTo(project.dokka.dokkaPublications)
            }

            dokkaSourceSets?.forEach { dokkaSourceSet ->
                dokkaSourceSet.applyTo(project.dokka.dokkaSourceSets)
            }

            pluginsConfiguration?.forEach { pluginConfiguration ->
                (pluginConfiguration as DokkaPluginParametersBaseSpec<org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec>).applyTo(project.dokka.pluginsConfiguration)
            }

            project.dokka.dokkaEngineVersion tryAssign dokkaEngineVersion
            project.dokka.dokkaGeneratorIsolation tryAssign dokkaGeneratorIsolation?.toWorkerIsolation()
        }
}
