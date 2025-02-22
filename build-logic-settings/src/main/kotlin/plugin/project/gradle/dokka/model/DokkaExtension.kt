package plugin.project.gradle.dokka.model

import gradle.maybeNamed
import gradle.tryAssign
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaExtension

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
     * Default: the [current project name][org.gradle.api.Project.name].
     */
    val moduleName: String?

    /**
     * The displayed module version.
     *
     * Default: the [version of the current project][org.gradle.api.Project.version].
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
     * Default: the current project's [path][org.gradle.api.Project.getPath] as a file path.
     */
    val modulePath: String?

    /**
     * An arbitrary string used to group source sets that originate from different Gradle subprojects.
     *
     * This is primarily used by Kotlin Multiplatform projects, which can have multiple source sets
     * per subproject.
     *
     * Defaults to [the Gradle path of the subproject][org.gradle.api.Project.getPath].
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
    val dokkaPublications: List<DokkaPublication>?

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
    val dokkaSourceSets: List<DokkaSourceSetSpec>?

    /**
     * The default version of Dokka dependencies that are used at runtime during generation.
     *
     * This value defaults to the current Dokka Gradle Plugin version, but can be overridden
     * if you want to use a newer or older version of Dokka at runtime.
     */
    val dokkaEngineVersion: String?

    context(Project)
    fun applyTo(extension: DokkaExtension) {
        extension.basePublicationsDirectory tryAssign basePublicationsDirectory?.let(layout.projectDirectory::dir)
        extension.dokkaCacheDirectory tryAssign dokkaCacheDirectory?.let(layout.projectDirectory::dir)
        extension.moduleName tryAssign moduleName
        extension.moduleVersion tryAssign moduleVersion
        extension.modulePath tryAssign modulePath
        extension.sourceSetScopeDefault tryAssign sourceSetScopeDefault
        extension.konanHome tryAssign konanHome?.let(::file)

        dokkaPublications?.forEach { dokkaPublication ->
            dokkaPublication.formatName.takeIf(String::isNotEmpty)?.also { formatName ->
                extension.dokkaPublications.maybeNamed(formatName) {
                    dokkaPublication.applyTo(this)
                }
            } ?: extension.dokkaPublications.configureEach {
                dokkaPublication.applyTo(this)
            }
        }

        dokkaSourceSets?.forEach { dokkaSourceSet ->
            dokkaSourceSet.name.takeIf(String::isNotEmpty)?.also { name ->
                extension.dokkaSourceSets.maybeNamed(name) {
                    dokkaSourceSet.applyTo(this)
                }
            } ?: extension.dokkaSourceSets.configureEach {
                dokkaSourceSet.applyTo(this)
            }
        }

        extension.dokkaEngineVersion tryAssign dokkaEngineVersion
    }
}
