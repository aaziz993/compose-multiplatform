package gradle

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project

/**
 * Types can implement this interface and use the embedded {@link Namer} implementation, to satisfy API that calls for a namer.
 */
internal interface Named {

    /**
     * The object's name.
     * <p>
     * Must be constant for the life of the object.
     *
     * @return The name. Never null.
     */
    val name: String

    context(Project)
    fun applyTo(named: Named)

    context(Project)
    fun applyTo(named: NamedDomainObjectCollection<out Named>, create: ((name: String) -> Named?)?) =
        named.configure(name, create) {
            applyTo(this)
        }

    context(Project)
    fun applyTo(named: NamedDomainObjectCollection<out Named>) = applyTo(named, null)

    context(Project)
    fun applyTo() {
        throw UnsupportedOperationException()
    }
}

private inline fun <reified T> NamedDomainObjectCollection<out T>.configure(name: String, noinline create: ((name: String) -> T?)? = null, noinline configure: T.() -> Unit) {
    if (name.isEmpty()) all(configure)
    else maybeNamed(name, configure) ?: create?.invoke(name)?.configure()
}
