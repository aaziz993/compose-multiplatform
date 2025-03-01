package gradle

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

internal inline fun <reified T> NamedDomainObjectContainer<out T>.maybeRegister(name: String, noinline configure: T.() -> Unit): NamedDomainObjectProvider<out T> =
    if (name in names) named(name, configure) else register(name, configure)

internal inline fun <reified T> NamedDomainObjectContainer<out T>.optionalRegister(name: String, noinline configure: T.() -> Unit): NamedDomainObjectProvider<out T>? =
    if (name in names) null else register(name, configure)

internal fun <T> NamedDomainObjectCollection<out T>.maybeNamed(name: String): NamedDomainObjectProvider<out T>? =
    if (name in names) named(name) else null

internal inline fun <reified T> NamedDomainObjectCollection<out T>.maybeNamed(name: String, noinline configure: T.() -> Unit): NamedDomainObjectProvider<out T>? =
    if (name in names) {
        named(name, configure)
    }
    else {
        null
    }

internal inline fun <reified T> NamedDomainObjectContainer<out T>.namedOrAll(name: String, noinline configure: T.() -> Unit) =
    if (name.isEmpty()) all(configure) else named(name, configure)

internal inline fun <reified T> NamedDomainObjectContainer<out T>.maybeNamedOrAll(name: String, noinline configure: T.() -> Unit) =
    if (name.isEmpty()) all(configure) else maybeNamed(name, configure)

internal inline fun <T> NamedDomainObjectCollection<out T>.all(crossinline action: (T) -> Unit) =
    all { action(this) }

internal inline fun <T> NamedDomainObjectCollection<out T>.configureEach(crossinline action: (T) -> Unit) =
    configureEach { action(this) }
