package gradle.api.services

import org.gradle.api.services.BuildServiceRegistry
import org.gradle.api.services.internal.RegisteredBuildServiceProvider

internal fun BuildServiceRegistry.unregister(name: String) {
    val registration = registrations.getByName(name)
    registrations.remove(registration)
    (registration.service as RegisteredBuildServiceProvider<*, *>).maybeStop()
}
