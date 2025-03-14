package gradle.api.repositories

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.UrlArtifactRepository

/**
 * A repository that supports resolving artifacts from a URL.
 *
 * @since 6.0
 */
internal interface UrlArtifactRepository {

    /**
     * Sets the base URL of this repository.
     *
     * @param url The base URL.
     */
    val url: String?

    /**
     * Specifies whether it is acceptable to communicate with a repository over an insecure HTTP connection.
     *
     *
     * For security purposes this intentionally requires a user to opt-in to using insecure protocols on case by case basis.
     *
     *
     * Gradle intentionally does not offer a global system/gradle property that allows a universal disable of this check.
     *
     *
     * **Allowing communication over insecure protocols allows for a man-in-the-middle to impersonate the intended server,
     * and gives an attacker the ability to
     * [serve malicious executable code onto the system.](https://max.computer/blog/how-to-take-over-the-computer-of-any-java-or-clojure-or-scala-developer/)
     ** *
     *
     *
     * See also:
     * [Want to take over the Java ecosystem? All you need is a MITM!](https://medium.com/bugbountywriteup/want-to-take-over-the-java-ecosystem-all-you-need-is-a-mitm-1fc329d898fb)
     */
    val allowInsecureProtocol: Boolean?

    fun applyTo(repository: UrlArtifactRepository) {
        url?.let(repository::setUrl)
        allowInsecureProtocol?.let(repository::setAllowInsecureProtocol)
    }
}
