package gradle.api.repositories

import org.gradle.api.artifacts.repositories.AuthenticationSupported
import org.gradle.api.file.Directory

/**
 * An artifact repository which supports username/password authentication.
 */
internal interface AuthenticationSupported<T: AuthenticationSupported> {

    /**
     * Configures the username and password credentials for this repository using the supplied action.
     *
     *
     * If no credentials have been assigned to this repository, an empty set of username and password credentials is assigned to this repository and passed to the action.
     * <pre class='autoTested'>
     * repositories {
     * maven {
     * url = "https://example.com/m2"
     * credentials {
     * username = 'joe'
     * password = 'secret'
     * }
     * }
     * }
    </pre> *
     *
     * @throws IllegalStateException when the credentials assigned to this repository are not of type [PasswordCredentials]
     */
    val credentials: PasswordCredentials?

    context(Directory)
    fun _applyTo(recipient: T) {
        credentials?.applyTo(recipient.credentials)
    }
}
