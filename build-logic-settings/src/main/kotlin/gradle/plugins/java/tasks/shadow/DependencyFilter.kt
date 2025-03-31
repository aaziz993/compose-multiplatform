package gradle.plugins.java.tasks.shadow

import gradle.accessors.files
import gradle.api.artifacts.ResolvedDependency
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project

@Serializable
internal data class DependencyFilter(
    /**
     * Resolve a FileCollection against the include/exclude rules in the filter
     * @param configuration
     * @return
     */
    val resolve: List<Set<String>>? = null,
    /**
     * Exclude dependencies that match the provided spec.
     *
     * @param spec
     * @return
     */
    val exclude: ResolvedDependency? = null,
    /**
     * Include dependencies that match the provided spec.
     *
     * @param spec
     * @return
     */
    val include: ResolvedDependency? = null,
) {

    context(Project)
    fun applyTo(receiver: com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter) {
        resolve?.map(project::files)?.let(receiver::resolve)

        receiver.exclude {
            exclude?.equals(it) != false
        }

        receiver.include {
            include?.equals(it) != false
        }
    }
}

internal object DependencyFilterContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) Boolean.serializer() else DependencyFilter.serializer()
}
