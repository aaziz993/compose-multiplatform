package gradle.api

import org.gradle.api.InvalidUserDataException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

@Throws(InvalidUserDataException::class)
internal inline fun <reified T> NamedDomainObjectContainer<T>.maybeRegister(
    name: String, noinline configurationAction: T.() -> Unit)
    : NamedDomainObjectProvider<T>? =
    if (name !in names) register(name, configurationAction) else null

@Throws(InvalidUserDataException::class)
internal inline fun <reified T> NamedDomainObjectContainer<T>.maybeRegister(name: String)
    : NamedDomainObjectProvider<T>? =
    if (name !in names) register(name) else null
