@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java.tasks.shadow

import gradle.api.tasks.copy.CopySpec
import gradle.reflect.trySet
import gradle.plugins.java.tasks.DependencyFilter
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project

internal interface ShadowSpec<T : com.github.jengelman.gradle.plugins.shadow.tasks.ShadowSpec> : CopySpec<T> {

    val relocators: Set<Relocator>?

    val dependencyFilter: DependencyFilter?

    val minimize: Boolean?

    val dependencyFilterForMinimize: DependencyFilter?

    /**
     * Syntactic sugar for merging service files in JARs.
     *
     * @return this
     */
    val mergeServiceFiles: @Serializable(with = MergeServiceFilesContentPolymorphicSerializer::class) Any?

    /**
     * Syntax sugar for merging service files in JARs
     *
     * @return this
     */
    val append: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        relocators?.map(Relocator::toRelocator)?.forEach(receiver::relocate)

        dependencyFilter?.let { dependencyFilter ->
            receiver.dependencies {
                dependencyFilter.applyTo(this)
            }
        }

        receiver::minimize trySet minimize
        DependencyFilter
        dependencyFilterForMinimize?.let { dependencyFilterForMinimize ->
            receiver.minimize {
                dependencyFilterForMinimize.applyTo(this)
            }
        }

        when (mergeServiceFiles) {
            is Boolean -> receiver.mergeServiceFiles()

            is String -> mergeServiceFiles?.let { mergeServiceFiles ->
                receiver.mergeServiceFiles(mergeServiceFiles)
            }

            else -> Unit
        }

        append?.let { append ->
            receiver.append(append)
        }
    }
}

private object MergeServiceFilesContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element.jsonPrimitive.isString) String.serializer() else Boolean.serializer()
}
