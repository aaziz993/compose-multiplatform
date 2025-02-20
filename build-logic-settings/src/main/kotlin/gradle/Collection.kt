package gradle

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

internal fun <T> Collection<T>.forEachEndAware(block: (Boolean, T) -> Unit) =
    forEachIndexed { index, it -> if (index == size - 1) block(true, it) else block(false, it) }

internal fun <T, V> Collection<T>.mapStartAware(block: (Boolean, T) -> V) =
    mapIndexed { index, it -> if (index == 0) block(true, it) else block(false, it) }

internal fun <T> NamedDomainObjectContainer<T>.maybeRegister(name: String, configure: T.() -> Unit): NamedDomainObjectProvider<T> =
    if (name in names) named(name, configure) else register(name, configure)

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String, configure: T.() -> Unit) {
    if (name in names) named(name).configure(configure)
}

internal inline fun <reified T> NamedDomainObjectCollection<*>.findByName(name: String): T? =
    findByName(name) as? T

internal fun <T> NamedDomainObjectCollection<T>.all(action: (T) -> Unit) =
    all { action(this) }

internal fun <T> NamedDomainObjectCollection<T>.configureEach(action: (T) -> Unit) =
    configureEach { action(this) }
