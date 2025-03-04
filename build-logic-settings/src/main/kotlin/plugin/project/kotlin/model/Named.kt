package plugin.project.kotlin.model

import gradle.maybeNamedOrAll
import gradle.namedOrAll
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
    fun applyTo(named: org.gradle.api.Named)

    context(Project)
    fun applyTo(named: NamedDomainObjectCollection<out org.gradle.api.Named>) =
        named.maybeNamedOrAll(name) {
            applyTo(this)
        }

    context(Project)
    fun applyTo() {
        throw UnsupportedOperationException()
    }
}
