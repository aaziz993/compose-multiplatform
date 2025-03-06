package gradle.model

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

    fun create(block: (name: String) -> Named): Named? =
        if (name.isNotEmpty()) block(name) else null

    context(Project)
    fun applyTo(named: Named)

    context(Project)
    fun applyTo(named: NamedDomainObjectCollection<out Named>) =
        named.configure(name) {
            applyTo(this)
        }

    context(Project)
    fun applyTo() {
        throw UnsupportedOperationException()
    }
}

private inline fun <reified T> NamedDomainObjectCollection<out T>.configure(name: String, noinline configure: T.() -> Unit) {
    if (name.isEmpty()) all(configure) else findByName(name)?.configure()
}
