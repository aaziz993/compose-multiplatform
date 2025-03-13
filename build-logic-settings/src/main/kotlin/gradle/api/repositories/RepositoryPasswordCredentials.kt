package gradle.api.repositories

import gradle.plugins.credentials.PasswordCredentials
import kotlinx.serialization.Serializable

/**
 * A username/password credentials that can be used to login to password-protected remote repository.
 */
@Serializable
internal data class RepositoryPasswordCredentials(
    override val username: String? = null,
    override val password: String? = null,
) : PasswordCredentials
