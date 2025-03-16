package gradle.plugins.dokka

import gradle.api.Named
import gradle.api.tryAssign
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.engine.parameters.KotlinPlatform
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

/**
 * A [DokkaSourceSetSpec] controls how Dokka will view and rendered sources.
 *
 * Dokka will automatically discover source sets from Kotlin, Android, and Java projects.
 *
 * Source sets can be configured individually, or all at once.
 *
 * ```kotlin
 * // build.gradle.kts
 *
 * dokka {
 *   dokkaSourceSets {
 *     // configure individual source set by name
 *     named("customSourceSet") {
 *       // ...
 *     }
 *
 *     // configure all source sets at once
 *     configureEach {
 *       // ...
 *     }
 *   }
 * }
 * ```
 *
 * @see org.jetbrains.dokka.DokkaSourceSetImpl
 */
@Serializable
internal data class DokkaSourceSetSpec(
    override val name: String = "",
    /**
     * An arbitrary string used to group source sets that originate from different Gradle subprojects.
     * This is primarily used by Kotlin Multiplatform projects, which can have multiple source sets
     * per subproject.
     *
     * The default is set from [DokkaExtension.sourceSetScopeDefault][org.jetbrains.dokka.gradle.DokkaExtension.sourceSetScopeDefault].
     *
     * It's unlikely that this value needs to be changed.
     */
    val sourceSetScope: String? = null,
    /**
     * Whether this source set should be skipped when generating documentation.
     *
     * Default is `false`.
     */
    val suppress: Boolean? = null,
    /**
     * Display name used to refer to the source set.
     *
     * The name will be used both externally (for example, source set name visible to documentation readers) and
     * internally (for example, for logging messages of [reportUndocumented]).
     *
     * By default, the value is deduced from information provided by the Kotlin Gradle plugin.
     */
    val displayName: String? = null,
    /**
     * List of Markdown files that contain
     * [module and package documentation](https://kotlinlang.org/docs/reference/dokka-module-and-package-docs.html).
     *
     * Contents of specified files will be parsed and embedded into documentation as module and package descriptions.
     *
     * Example of such a file:
     *
     * ```markdown
     * # Module kotlin-demo
     *
     * The module shows the Dokka usage.
     *
     * # Package org.jetbrains.kotlin.demo
     *
     * Contains assorted useful stuff.
     *
     * ## Level 2 heading
     *
     * Text after this heading is also part of documentation for `org.jetbrains.kotlin.demo`
     *
     * # Package org.jetbrains.kotlin.demo2
     *
     * Useful stuff in another package.
     * ```
     */
    val includes: List<String>? = null,
    /**
     * Set of visibility modifiers that should be documented.
     *
     * This can be used if you want to document protected/internal/private declarations,
     * as well as if you want to exclude public declarations and only document internal API.
     *
     * Can be configured on per-package basis, see [DokkaPackageOptionsSpec.documentedVisibilities].
     *
     * Default is [VisibilityModifier.Public].
     */
    val documentedVisibilities: List<VisibilityModifier>? = null,
    /**
     * Classpath for analysis and interactive samples.
     *
     * Useful if some types that come from dependencies are not resolved/picked up automatically.
     * Property accepts both `.jar` and `.klib` files.
     *
     * By default, classpath is deduced from information provided by the Kotlin Gradle plugin.
     */
    val classpath: List<String>? = null,
    /**
     * Source code roots to be analyzed and documented.
     * Accepts directories and individual `.kt` / `.java` files.
     *
     * By default, source roots are deduced from information provided by the Kotlin Gradle plugin.
     */
    val sourceRoots: List<String>? = null,
    /**
     * List of directories or files that contain sample functions which are referenced via
     * [`@sample`](https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier) KDoc tag.
     */
    val samples: List<String>? = null,
    /**
     * Whether to emit warnings about visible undocumented declarations, that is declarations without KDocs
     * after they have been filtered by [documentedVisibilities].
     *
     * Can be overridden for a specific package by setting [DokkaPackageOptionsSpec.reportUndocumented].
     *
     * Default is `false`.
     */
    val reportUndocumented: Boolean? = null,
    /**
     * Specifies the location of the project source code on the Web. If provided, Dokka generates
     * "source" links for each declaration. See [DokkaSourceLinkSpec] for more details.
     *
     * Prefer using [sourceLink] action/closure for adding source links.
     *
     * @see sourceLink
     */
    val sourceLinks: Set<DokkaSourceLinkSpec>? = null,
    /**
     * Allows customising documentation generation options on a per-package basis.
     *
     * Use the [perPackageOptions] function to add a new item.
     *
     * @see DokkaPackageOptionsSpec
     */
    val perPackageOptions: Set<DokkaPackageOptionsSpec>? = null,
    /**
     * Allows linking to Dokka/Javadoc documentation of the project's dependencies.
     */
    val externalDocumentationLinks: List<DokkaExternalDocumentationLinkSpec>? = null,
    /**
     * Platform to be used for setting up code analysis and samples.
     *
     * The default value is deduced from information provided by the Kotlin Gradle plugin.
     */
    val analysisPlatform: KotlinPlatform? = null,
    /**
     * Whether to skip packages that contain no visible declarations after
     * various filters have been applied.
     *
     * For instance, if [skipDeprecated] is set to `true` and your package contains only
     * deprecated declarations, it will be considered to be empty.
     *
     * Default is `true`.
     */
    val skipEmptyPackages: Boolean? = null,
    /**
     * Whether to document declarations annotated with [Deprecated].
     *
     * Can be overridden on package level by setting [DokkaPackageOptionsSpes.skipDeprecated].
     *
     * Default is `false`.
     */
    val skipDeprecated: Boolean? = null,
    /**
     * Directories or individual files that should be suppressed, meaning declarations from them
     * will be not documented.
     *
     * Will be concatenated with generated files if [suppressGeneratedFiles] is set to `false`.
     */
    val suppressedFiles: List<String>? = null,
    /**
     * Whether to document/analyze generated files.
     *
     * Generated files are expected to be present under `{project}/{buildDir}/generated` directory.
     * If set to `true`, it effectively adds all files from that directory to [suppressedFiles], so
     * you can configure it manually.
     *
     * Default is `true`.
     */
    val suppressGeneratedFiles: Boolean? = null,
    /**
     * Whether to generate external documentation links that lead to API reference documentation for
     * Kotlin's standard library when declarations from it are used.
     *
     * Default is `true`, meaning links will be generated.
     *
     * @see externalDocumentationLinks
     */
    val enableKotlinStdLibDocumentationLink: Boolean? = null,
    /**
     * Whether to generate external documentation links to JDK's Javadocs when declarations from it
     * are used.
     *
     * The version of JDK Javadocs is determined by [jdkVersion] property.
     *
     * Default is `true`, meaning links will be generated.
     *
     * @see externalDocumentationLinks
     */
    val enableJdkDocumentationLink: Boolean? = null,
    /**
     * Whether to generate external documentation links for Android SDK API reference when
     * declarations from it are used.
     *
     * Only relevant in Android projects, and will be automatically disabled otherwise.
     *
     * The default value is automatically determined.
     * If [analysisPlatform] is set to [KotlinPlatform.AndroidJVM], then the value will be `true`.
     * Otherwise, the value defaults to `false`.
     *
     * @see externalDocumentationLinks
     */
    val enableAndroidDocumentationLink: Boolean? = null,
    /**
     * [Kotlin language version](https://kotlinlang.org/docs/compatibility-modes.html)
     * used for setting up analysis and [`@sample`](https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier)
     * environment.
     *
     * By default, the latest language version available to Dokka's embedded compiler will be used.
     */
    val languageVersion: String? = null,
    /**
     * [Kotlin API version](https://kotlinlang.org/docs/compatibility-modes.html)
     * used for setting up analysis and [`@sample`](https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier)
     * environment.
     *
     * By default, it will be deduced from [languageVersion].
     */
    val apiVersion: String? = null,
    /**
     * JDK version to use when generating external documentation links for Java types.
     *
     * For instance, if you use [java.util.UUID] from JDK in some public declaration signature,
     * and this property is set to `8`, Dokka will generate an external documentation link
     * to [JDK 8 Javadocs](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) for it.
     *
     * Default is JDK 11.
     */
    val jdkVersion: Int? = null,
) : Named {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        named as org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec

        named.sourceSetScope tryAssign sourceSetScope
        named.suppress tryAssign suppress
        named.displayName tryAssign displayName
        includes?.let(named.includes::setFrom)
        named.documentedVisibilities tryAssign documentedVisibilities
        classpath?.let(named.classpath::setFrom)
        sourceRoots?.let(named.sourceRoots::setFrom)
        samples?.let(named.samples::setFrom)
        named.reportUndocumented tryAssign reportUndocumented

        sourceLinks?.forEach { sourceLink ->
            named.sourceLink {
                sourceLink.applyTo(this)
            }
        }

        perPackageOptions?.forEach { perPackageOption ->
            named.perPackageOption(perPackageOption::applyTo)
        }

        externalDocumentationLinks?.forEach { externalDocumentationLink ->
            externalDocumentationLink.applyTo(named.externalDocumentationLinks)
        }

        named.analysisPlatform tryAssign analysisPlatform
        named.skipEmptyPackages tryAssign skipEmptyPackages
        named.skipDeprecated tryAssign skipDeprecated
        suppressedFiles?.let(named.suppressedFiles::setFrom)
        named.suppressGeneratedFiles tryAssign suppressGeneratedFiles
        named.enableKotlinStdLibDocumentationLink tryAssign enableKotlinStdLibDocumentationLink
        named.enableJdkDocumentationLink tryAssign enableJdkDocumentationLink
        named.enableAndroidDocumentationLink tryAssign enableAndroidDocumentationLink
        named.languageVersion tryAssign languageVersion
        named.apiVersion tryAssign apiVersion
        named.jdkVersion tryAssign jdkVersion
    }
}

internal object DokkaSourceSetSpecTransformingSerializer : KeyTransformingSerializer<DokkaSourceSetSpec>(
    DokkaSourceSetSpec.serializer(),
    "name",
)
