package gradle.api.credentials

import org.gradle.api.credentials.Credentials

internal interface Credentials<T : Credentials> {

    fun applyTo(receiver: T)
}
