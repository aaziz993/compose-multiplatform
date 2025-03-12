package gradle.api

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container

internal inline fun <reified T : Named> Project.containerize(vararg values: T) =
    container { name ->
        values.single { value -> value.name == name }
    }.apply {
        values.forEach { value ->
            create(value.name)
        }
    }

internal inline fun <T> NamedDomainObjectCollection<out T>.all(crossinline action: (T) -> Unit) =
    all { action(this) }

internal inline fun <T> NamedDomainObjectCollection<out T>.configureEach(crossinline action: (T) -> Unit) =
    configureEach { action(this) }

internal inline fun <reified T> NamedDomainObjectCollection<out T>.maybeNamed(name: String, noinline configure: T.() -> Unit): NamedDomainObjectProvider<out T>? =
    if (name in names) {
        named(name, configure)
    }
    else {
        null
    }

internal inline fun <reified S : T, T> NamedDomainObjectCollection<T>.maybeNamed(name: String, type: Class<S>, noinline configure: S.() -> Unit) =
    if (name in names) {
        named(name, type, configure)
    }
    else {
        null
    }
