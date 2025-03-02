package gradle

import kotlin.reflect.KClass
import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.tasks.TaskProvider
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

internal inline fun <reified T> NamedDomainObjectCollection<out T>.namedOrAll(name: String, noinline configure: T.() -> Unit) {
    if (name.isEmpty()) all(configure) else named(name, configure)
}

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

internal inline fun <reified T> NamedDomainObjectCollection<out T>.maybeNamedOrAll(name: String, noinline configure: T.() -> Unit) {
    if (name.isEmpty()) all(configure) else maybeNamed(name, configure)
}
