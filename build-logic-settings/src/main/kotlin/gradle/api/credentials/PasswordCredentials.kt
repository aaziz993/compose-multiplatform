package gradle.api.credentials

import gradle.api.trySet
import org.gradle.api.credentials.PasswordCredentials

/**
 * A username/password credentials that can be used to login to something protected by a username and password.
 *
 * @since 3.5
 */
internal interface PasswordCredentials<T : PasswordCredentials> : Credentials<T> {

    /**
     * Sets the user name to use when authenticating.
     *
     * @param userName The user name. May be null.
     */
    val username: String?

    /**
     * Sets the password to use when authenticating.
     *
     * @param password The password. May be null.
     */
    val password: String?

    override fun applyTo(receiver: T) {
        receiver::setUsername trySet username
        receiver::setPassword trySet password
    }
}
