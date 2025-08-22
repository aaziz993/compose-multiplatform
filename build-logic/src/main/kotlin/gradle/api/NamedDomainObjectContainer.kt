package gradle.api

import org.gradle.api.Action
import org.gradle.api.InvalidUserDataException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

@Throws(InvalidUserDataException::class)
public fun <T : Any> NamedDomainObjectContainer<T>.maybeRegister(
    name: String, configurationAction: Action<in T>
): NamedDomainObjectProvider<T>? = if (name !in names) register(name, configurationAction) else null

@Throws(InvalidUserDataException::class)
public fun <T : Any> NamedDomainObjectContainer<T>.maybeRegister(name: String): NamedDomainObjectProvider<T>? =
    if (name !in names) register(name) else null
