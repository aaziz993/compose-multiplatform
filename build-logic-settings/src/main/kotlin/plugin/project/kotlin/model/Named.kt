package plugin.project.kotlin.model

import gradle.maybeNamedOrAll
import gradle.namedOrAll
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer

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
}

context(Named)
internal inline fun <reified T> NamedDomainObjectCollection<out T>.configure(noinline configure: T.() -> Unit) =
    namedOrAll(name, configure)

context(Named)
internal inline fun <reified T> NamedDomainObjectCollection<out T>.maybeConfigure(noinline configure: T.() -> Unit) =
    maybeNamedOrAll(name, configure)
