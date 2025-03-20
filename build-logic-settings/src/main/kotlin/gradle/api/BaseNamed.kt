package gradle.api

import org.gradle.api.Action
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
    val name: String
}

internal interface SettingsNamed<T> : BaseNamed {

    context(Settings)
    fun applyTo(recipient: T)
}

context(Settings)
internal fun <T> SettingsNamed<T>.applyTo(
    recipient: NamedDomainObjectCollection<T>,
    create: (name: String, action: Action<in T>) -> Unit
) = recipient.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Settings)
internal fun <T> SettingsNamed<T>.applyTo(recipient: NamedDomainObjectContainer<T>) =
    applyTo(recipient) { name, action ->
        recipient.register(name, action)
    }

internal interface ProjectNamed<T> : BaseNamed {

    context(Project)
    fun applyTo(recipient: T)
}

context(Project)
internal fun <T> ProjectNamed<T>.applyTo(
    recipient: NamedDomainObjectCollection<T>,
    create: (name: String, action: Action<in T>) -> Unit
) = recipient.maybeNamedCreateOrEach(name, create) {
    applyTo(this)
}

context(Project)
internal fun <T> ProjectNamed<T>.applyTo(recipient: NamedDomainObjectContainer<T>) =
    applyTo(recipient) { name, action ->
        recipient.register(name, action)
    }

internal interface Named<T> : SettingsNamed<T>, ProjectNamed<T> {

    context(Settings)
    override fun applyTo(recipient: T)

    context(Project)
    override fun applyTo(recipient: T)
}

internal fun <T> NamedDomainObjectCollection<T>.maybeNamedCreateOrEach(
    name: String,
    create: (name: String, action: Action<in T>) -> Unit = { _, _ -> },
    configureAction: Action<in T>
) {
    if (name.isEmpty()) configureEach(configureAction)
    else (maybeNamed(name, configureAction) ?: create(name, configureAction))
}

internal interface Test<in T> {

    fun t(l: Collection<T>) {}
}
