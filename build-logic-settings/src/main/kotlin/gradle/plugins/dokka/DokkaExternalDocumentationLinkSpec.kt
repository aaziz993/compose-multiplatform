package gradle.plugins.dokka


import gradle.api.BaseNamed
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.dokka.gradle.engine.parameters.DokkaExternalDocumentationLinkSpec

/**
 * Configuration builder that allows creating links leading to externally hosted
 * documentation of your dependencies.
 *
 * For instance, if you are using types from `kotlinx.serialization`, by default
 * they will be unclickable in your documentation, as if unresolved. However,
 * since API reference for `kotlinx.serialization` is also built by Dokka and is
 * [published on kotlinlang.org](https://kotlinlang.org/api/kotlinx.serialization/),
 * you can configure external documentation links for it, allowing Dokka to generate
 * documentation links for used types, making them clickable and appear resolved.
 *
 * Example in Gradle Kotlin DSL:
 *
 * ```kotlin
 * externalDocumentationLink {
 *  url.set(URI("https://kotlinlang.org/api/kotlinx.serialization/"))
 *  packageListUrl.set(
 *    rootProject.projectDir.resolve("serialization.package.list").toURI()
 *  )
 * }
 * ```
 */
@Serializable
internal data class DokkaExternalDocumentationLinkSpec(
    override val name: String = "",
    /**
     * Root URL of documentation to link with.
     *
     * Dokka will do its best to automatically find `package-list` for the given URL, and link
     * declarations together.
     *
     * It automatic resolution fails or if you want to use locally cached files instead,
     * consider providing [packageListUrl].
     *
     * Example:
     *
     * ```kotlin
     * url.set(java.net.URI("https://kotlinlang.org/api/kotlinx.serialization/"))
     *
     * // OR
     *
     * url("https://kotlinlang.org/api/kotlinx.serialization/")
     * ```
     *
     * @see url
     */
    val url: String? = null,
    /**
     * Specifies the exact location of a `package-list` instead of relying on Dokka
     * automatically resolving it. Can also be a locally cached file to avoid network calls.
     *
     * Example:
     *
     * ```kotlin
     * packageListUrl.set(rootProject.projectDir.resolve("serialization.package.list").toURI())
     * ```
     */
    val packageListUrl: String? = null,
    /**
     * If enabled this link will be passed to the Dokka Generator.
     *
     * Defaults to `true`.
     *
     * @see org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec.enableKotlinStdLibDocumentationLink
     * @see org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec.enableJdkDocumentationLink
     * @see org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec.enableAndroidDocumentationLink
     */
    val enabled: Boolean? = null,
) : BaseNamed {

        context(Project)
    override fun applyTo(recipient: T) {
        named as DokkaExternalDocumentationLinkSpec

        url?.let(named::url)
        packageListUrl?.let(named::packageListUrl)
        named.enabled tryAssign enabled
    }
}
