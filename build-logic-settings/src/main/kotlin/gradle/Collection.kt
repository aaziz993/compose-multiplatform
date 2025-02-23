package gradle

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

internal fun <T> NamedDomainObjectContainer<T>.maybeRegister(name: String, configure: T.() -> Unit): NamedDomainObjectProvider<T> =
    if (name in names) named(name, configure) else register(name, configure)

internal fun <T> NamedDomainObjectContainer<T>.optionalRegister(name: String, configure: T.() -> Unit): NamedDomainObjectProvider<T>? =
    if (name in names) null else register(name, configure)

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null


internal inline fun <reified T> NamedDomainObjectCollection<T>.maybeNamed(name: String, noinline configure: T.() -> Unit): NamedDomainObjectProvider<T>? =
    if (name in names) {
        named(name, configure)
    }
    else {
        null
    }

internal fun <T> NamedDomainObjectCollection<T>.all(action: (T) -> Unit) =
    all { action(this) }

internal fun <T> NamedDomainObjectCollection<T>.configureEach(action: (T) -> Unit) =
    configureEach { action(this) }
