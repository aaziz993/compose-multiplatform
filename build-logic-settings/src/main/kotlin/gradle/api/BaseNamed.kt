package gradle.api

import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

internal interface BaseNamed {

    /**
     * The object's name.
     * <p>
     * Must be constant for the life of the object.
     *
     * @return The name. Never null.
     */
    val name: String?
}

internal interface SettingsNamed<T> : BaseNamed {

    context(Settings)
    fun applyTo(receiver: T)
}

context(Settings)
internal fun <T : Named> SettingsNamed<T>.applyTo(
    receiver: DomainObjectCollection<out T>,
    create: (name: String, action: Action<in T>) -> Unit
) = receiver.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Settings)
internal fun <T> SettingsNamed<T>.applyTo(
    receiver: NamedDomainObjectCollection<out T>,
    create: (name: String, action: Action<in T>) -> Unit
) = receiver.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Settings)
internal fun <T> SettingsNamed<T>.applyTo(receiver: NamedDomainObjectContainer<out T>) =
    applyTo(receiver) { name, action ->
        receiver.register(name, action)
    }

internal interface ProjectNamed<T> : BaseNamed {

    context(Project)
    fun applyTo(receiver: T)
}

context(Project)
internal fun <T : Named> ProjectNamed<T>.applyTo(
    receiver: DomainObjectCollection<out T>,
    create: (name: String, action: Action<in T>) -> Unit
) = receiver.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Project)
internal fun <T> ProjectNamed<T>.applyTo(
    receiver: NamedDomainObjectCollection<out T>,
    create: (name: String, action: Action<in T>) -> Unit
) = receiver.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Project)
internal fun <T> ProjectNamed<T>.applyTo(receiver: NamedDomainObjectContainer<out T>) =
    applyTo(receiver) { name, action ->
        receiver.register(name, action)
    }

internal interface Named<T> : SettingsNamed<T>, ProjectNamed<T> {

    context(Settings)
    override fun applyTo(receiver: T)

    context(Project)
    override fun applyTo(receiver: T)
}

internal fun <T : Named> DomainObjectCollection<out T>.maybeNamedCreateOrEach(
    name: String?,
    create: (name: String, action: Action<in T>) -> Unit = { _, _ -> },
    configureAction: Action<in T>
) {
    if (name.isNullOrBlank()) configureEach(configureAction)
    else matching { it.name == name }.let {
        if (it.isEmpty()) create(name, configureAction)
        else it.configureEach(configureAction)
    }
}

internal fun <T> NamedDomainObjectCollection<out T>.maybeNamedCreateOrEach(
    name: String?,
    create: (name: String, action: Action<in T>) -> Unit = { _, _ -> },
    configureAction: Action<in T>
) {
    if (name.isNullOrBlank()) configureEach(configureAction)
    else (maybeNamed(name, configureAction) ?: create(name, configureAction))
}
