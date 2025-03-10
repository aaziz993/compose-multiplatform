package gradle.model.gradle.publish

import gradle.model.PasswordCredentials

/**
 * A username/password credentials that can be used to login to password-protected remote repository.
 */
internal interface RepositoryPasswordCredentials : PasswordCredentials
