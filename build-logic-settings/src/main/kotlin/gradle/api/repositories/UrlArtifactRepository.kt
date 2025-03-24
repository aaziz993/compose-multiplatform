package gradle.api.repositories

import gradle.isUrl
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.UrlArtifactRepository
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

/**
 * A repository that supports resolving artifacts from a URL.
 *
 * @since 6.0
 */
internal interface UrlArtifactRepository<T: UrlArtifactRepository> {

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

    context(settings: Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: T) = with(settings.layout.settingsDirectory) {
        _applyTo(repository)
    }

    context(project: Project)
    fun applyTo(receiver: T) = with(project.layout.projectDirectory) {
        _applyTo(repository)
    }

    context(directory: Directory)
    fun _applyTo(receiver: T) {
        url?.let { url ->
            if (url.isUrl) url else directory.dir(url)
        }?.let(receiver::setUrl)
        allowInsecureProtocol?.let(receiver::setAllowInsecureProtocol)
    }
}
