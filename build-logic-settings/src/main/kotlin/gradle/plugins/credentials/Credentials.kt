package gradle.plugins.credentials

import org.gradle.api.credentials.Credentials

internal interface Credentials<T : Credentials> {

    fun applyTo(recipient: T)
}
