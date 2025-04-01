package gradle.api

import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container

public inline fun <reified T : Named> Project.containerize(vararg values: T) =
    container { name ->
        values.single { value -> value.name == name }
    }.apply {
        values.forEach { value ->
            create(value.name)
        }
    }

public fun <T> NamedDomainObjectCollection<T>.maybeRegister(name: String) =
    if (name.isEmpty()) toList() else listOf(getByName(name))

public fun <T> NamedDomainObjectCollection<T>.getByNameOrAll(name: String) =
    if (name.isEmpty()) toList() else listOf(getByName(name))

public fun <T> DomainObjectCollection<T>.all(action: (T & Any) -> Unit) =
    all(action)

public fun <T> DomainObjectCollection<T>.configureEach(action: (T & Any) -> Unit) =
    configureEach(action)

public fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null

public fun <T, S : T> NamedDomainObjectCollection<T>.maybeNamed(name: String, type: Class<S>): NamedDomainObjectProvider<S>? =
    if (name in names) named(name, type) else null

public fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String, configureAction: Action<in T>): NamedDomainObjectProvider<T>? =
    if (name in names) named(name, configureAction) else null

public fun <T, S : T> NamedDomainObjectCollection<T>.maybeNamed(name: String, type: Class<S>, configureAction: Action<in S>) =
    if (name in names) named(name, type, configureAction) else null

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public inline fun <reified T> NamedDomainObjectCollection<*>.findByName(name: String): T? = findByName(name) as? T
