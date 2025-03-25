package gradle.plugins.dokka

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.project.ProjectLayout
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceLinkSpec

/**
 * Configuration builder that allows adding a `source` link to each signature
 * which leads to [remoteUrl] with a specific line number (configurable by setting [remoteLineSuffix]),
 * letting documentation readers find source code for each declaration.
 *
 * Example in Gradle Kotlin DSL:
 *
 * ```kotlin
 * sourceLink {
 *   localDirectory.set(projectDir.resolve("src"))
 *   remoteUrl.set(URI("https://github.com/kotlin/dokka/tree/master/src"))
 *   remoteLineSuffix.set("#L")
 * }
 * ```
 */
@Serializable
internal data class DokkaSourceLinkSpec(
    /**
     * Path to the local source directory. The path must be relative to the root of current project.
     *
     * This path is used to find relative paths of the source files from which the documentation is built.
     * These relative paths are then combined with the base url of a source code hosting service specified with
     * the [remoteUrl] property to create source links for each declaration.
     *
     * Example:
     *
     * ```kotlin
     * projectDir.resolve("src")
     * ```
     */
    // changing contents of the directory should not invalidate the task
    val localDirectory: String? = null,
    /**
     * URL of source code hosting service that can be accessed by documentation readers,
     * like GitHub, GitLab, Bitbucket, etc. This URL will be used to generate
     * source code links of declarations.
     *
     * Example:
     *
     * ```kotlin
     * remoteUrl.set(java.net.URI("https://github.com/username/projectname/tree/master/src"))
     *
     * // OR
     *
     * remoteUrl("https://github.com/username/projectname/tree/master/src")
     * ```
     *
     * @see remoteUrl
     */
    val remoteUrl: String? = null,
    /**
     * Suffix used to append source code line number to the URL. This will help readers navigate
     * not only to the file, but to the specific line number of the declaration.
     *
     * The number itself will be appended to the specified suffix. For instance,
     * if this property is set to `#L` and the line number is 10, resulting URL suffix
     * will be `#L10`
     *
     * Suffixes used by popular services:
     * - GitHub: `#L`
     * - GitLab: `#L`
     * - Bitbucket: `#lines-`
     *
     * Default is `#L`.
     */
    val remoteLineSuffix: String? = null
) {

    context(project: Project)
    fun applyTo(receiver: DokkaSourceLinkSpec) {
        receiver.localDirectory tryAssign layout.projectDirectory.dir(
            localDirectory ?: when (projectProperties.layout) {
                ProjectLayout.FLAT -> ""
                else -> "src"
            },
        )
        (remoteUrl ?: settings.projectProperties.scm?.url)?.let(receiver::remoteUrl)
        receiver.remoteLineSuffix tryAssign remoteLineSuffix
    }
}
