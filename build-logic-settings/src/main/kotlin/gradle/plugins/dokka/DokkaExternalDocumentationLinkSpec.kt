package gradle.plugins.dokka

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.provider.tryAssign
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
@KeepGeneratedSerializer
@Serializable(with = DokkaExternalDocumentationLinkSpecObjectTransformingSerializer::class)
internal data class DokkaExternalDocumentationLinkSpec(
    override val name: String? = null,
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
) : ProjectNamed<org.jetbrains.dokka.gradle.engine.parameters.DokkaExternalDocumentationLinkSpec> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.dokka.gradle.engine.parameters.DokkaExternalDocumentationLinkSpec) {
        url?.let(receiver::url)
        packageListUrl?.let(receiver::packageListUrl)
        receiver.enabled tryAssign enabled
    }
}

internal object DokkaExternalDocumentationLinkSpecObjectTransformingSerializer
    : NamedObjectTransformingSerializer<DokkaExternalDocumentationLinkSpec>(
    DokkaExternalDocumentationLinkSpec.generatedSerializer(),
)
