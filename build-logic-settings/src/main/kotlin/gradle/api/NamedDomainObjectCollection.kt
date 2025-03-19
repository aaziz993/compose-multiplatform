package gradle.api

import java.lang.reflect.ParameterizedType
import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container

@Suppress("UNCHECKED_CAST")
internal fun <T> NamedDomainObjectCollection<T>.elementType() =
    (javaClass.getGenericSuperclass() as ParameterizedType)
        .actualTypeArguments[0] as Class<T>

internal inline fun <reified T : Named> Project.containerize(vararg values: T) =
    container { name ->
        values.single { value -> value.name == name }
    }.apply {
        values.forEach { value ->
            create(value.name)
        }
    }

internal fun <T> NamedDomainObjectCollection<T>.maybeRegister(name: String) =
    if (name.isEmpty()) toList() else listOf(getByName(name))

internal fun <T> NamedDomainObjectCollection<T>.getByNameOrAll(name: String) =
    if (name.isEmpty()) toList() else listOf(getByName(name))

internal fun <T> DomainObjectCollection<T>.all(action: (T & Any) -> Unit) =
    all(action)

internal fun <T> DomainObjectCollection<T>.configureEach(action: (T & Any) -> Unit) =
    configureEach(action)

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String): NamedDomainObjectProvider<T>? =
    if (name in names) named(name) else null

internal fun <T, S : T> NamedDomainObjectCollection<T>.maybeNamed(name: String, type: Class<S>): NamedDomainObjectProvider<S>? =
    if (name in names) named(name, type) else null

internal fun <T> NamedDomainObjectCollection<T>.maybeNamed(name: String, configureAction: Action<in T>): NamedDomainObjectProvider<T>? =
    if (name in names) named(name, configureAction) else null

internal fun <T, S : T> NamedDomainObjectCollection<T>.maybeNamed(name: String, type: Class<S>, configureAction: Action<in S>) =
    if (name in names) named(name, type, configureAction) else null

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
internal inline fun <reified T> NamedDomainObjectCollection<*>.findByName(name: String): T? = findByName(name) as? T
