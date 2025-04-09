@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java.tasks.shadow

import gradle.api.tasks.copy.CopySpec
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import klib.data.type.serialization.serializer.ContentPolymorphicSerializer

import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project

internal interface ShadowSpec<T : com.github.jengelman.gradle.plugins.shadow.tasks.ShadowSpec> : CopySpec<T> {

    val relocators: Set<Relocator>?

    val dependencies: DependencyFilter?

    val minimize: @Serializable(with = DependencyFilterContentPolymorphicSerializer::class) Any?

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

        dependencies?.let { dependencies ->
            receiver.dependencies {
                dependencies.applyTo(this)
            }
        }

        when (val minimize = minimize) {
            is Boolean -> receiver.minimize()

            is DependencyFilter -> receiver.minimize {
                minimize.applyTo(this)
            }

            else -> Unit
        }

        when (val mergeServiceFiles = mergeServiceFiles) {
            is Boolean -> receiver.mergeServiceFiles()
            is String -> receiver.mergeServiceFiles(mergeServiceFiles)
            else -> Unit
        }

        append?.let { append ->
            receiver.append(append)
        }
    }
}

internal object MergeServiceFilesContentPolymorphicSerializer :
    ContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(value: Any?): DeserializationStrategy<Any> =
        if (element.jsonPrimitive.isString) String.serializer() else Boolean.serializer()
}
