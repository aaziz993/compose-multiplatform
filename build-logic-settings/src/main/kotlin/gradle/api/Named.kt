package gradle.api

import gradle.accessors.publishing
import java.lang.reflect.ParameterizedType
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskCollection
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

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
    fun applyTo(named: NamedDomainObjectContainer<out Named>) =
        named.namedOrAll(name) {
            applyTo(this)
        }

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun <T : Task> applyTo(named: TaskCollection<T>) =
        named.namedOrAll(
            name,
            { name ->
                val taskClass: Class<out Task> = (named.javaClass.getGenericSuperclass() as ParameterizedType)
                    .actualTypeArguments[0] as Class<out Task>
                tasks.register(name, taskClass).get()
            },
        ) {
            applyTo(this)
        }

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(named: PublicationContainer) =
        named.namedOrAll(name, { name -> publishing.publications.create<MavenPublication>(name) }) {
            applyTo(this)
        }

    context(Project)
    fun <T : KotlinTarget> applyTo(named: NamedDomainObjectCollection<T>, create: (name: String) -> T) =
        named.namedOrAll(name) {
            applyTo(this)
        }

    context(Project)
    fun applyTo(named: NamedDomainObjectCollection<out Named>) =
        named.namedOrAll(name) {
            applyTo(this)
        }

    context(Project)
    fun applyTo() {
        throw UnsupportedOperationException()
    }
}

internal inline fun <reified T> NamedDomainObjectCollection<out T>.namedOrAll(
    name: String,
    create: (name: String) -> T? = { null },
    noinline configure: T.() -> Unit
) {
    if (name.isEmpty()) all(configure)
    else (maybeNamed(name, configure) ?: create(name)?.configure())
}
