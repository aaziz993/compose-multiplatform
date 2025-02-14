package gradle

import kotlin.reflect.KProperty
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

internal fun <T> NamedDomainObjectContainer<T>.maybeRegister(name: String, configure: T.() -> Unit): NamedDomainObjectProvider<T> =
    if (name in names) named(name, configure) else register(name, configure)

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String, configure: T.() -> Unit) {
    if (name in names) named(name).configure(configure)
}

internal inline fun <reified T> NamedDomainObjectCollection<*>.findByName(name: String): T? =
    findByName(name) as? T

