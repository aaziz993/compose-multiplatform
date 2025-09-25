package gradle.api

import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container

public inline fun <reified T : Named> Project.containerize(vararg values: T): NamedDomainObjectContainer<T> =
    container { name ->
        values.single { value -> value.name == name }
    }.apply {
        values.forEach { value ->
            create(value.name)
        }
    }

public fun <T : Any> NamedDomainObjectCollection<T>.getByNameOrAll(name: String): List<T> =
    if (name.isEmpty()) toList() else listOf(getByName(name))

public fun <T : Any> DomainObjectCollection<T>.all(action: (T) -> Unit): Unit = all(action)

public fun <T : Any> DomainObjectCollection<T>.configureEach(action: (T) -> Unit): Unit = configureEach(action)

public fun <T : Any> DomainObjectCollection<T>.configure(action: (T) -> Unit): Unit = configure(action)

public fun <T : Any> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null

public fun <S : T, T : Any> NamedDomainObjectCollection<T>.maybeNamed(
    name: String,
    type: Class<S>
): NamedDomainObjectProvider<S>? = if (name in names) named(name, type) else null

public fun <T : Any> NamedDomainObjectCollection<T>.maybeNamed(
    name: String,
    configureAction: Action<in T>
): NamedDomainObjectProvider<T>? =
    if (name in names) named(name, configureAction) else null

public fun <S : T, T : Any> NamedDomainObjectCollection<T>.maybeNamed(
    name: String,
    type: Class<S>,
    configureAction: Action<in S>
): NamedDomainObjectProvider<S>? = if (name in names) named(name, type, configureAction) else null

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public inline fun <reified T : Any> NamedDomainObjectCollection<*>.findByName(name: String): T? = findByName(name) as? T
