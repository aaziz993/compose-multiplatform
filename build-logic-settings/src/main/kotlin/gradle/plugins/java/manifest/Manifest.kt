package gradle.plugins.java.manifest

import klib.data.type.act
import klib.data.type.serialization.serializer.SerializableAnyMap
import klib.data.type.collection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class Manifest(
    val attributes: SerializableAnyMap? = null,
    val setAttributes: SerializableAnyMap? = null,
    val sections: Map<String, SerializableAnyMap>? = null,
    val setSections: Map<String, SerializableAnyMap>? = null,
    val effectiveManifest: Manifest? = null,
    val from: @Serializable(with = FromContentPolymorphicSerializer::class) Any? = null,
) {

    context(Project)
    fun applyTo(receiver: org.gradle.api.java.archives.Manifest) {
        receiver.attributes.putAll(
            mapOf(
                "Implementation-Title" to (description ?: name),
                "Implementation-Version" to version,
                "Automatic-Module-Name" to name,
            ),
        )

        attributes?.let(receiver::attributes)
        receiver.attributes trySet setAttributes

        sections?.forEach { (key, value) ->
            receiver.attributes(value, key)
        }

        setSections?.act(receiver.sections::clear)?.forEach { (key, value) ->
            receiver.attributes(value, key)
        }

        effectiveManifest?.applyTo(receiver.effectiveManifest)

        when (from) {
            is String, is From -> setOf(from)
            is Set<*> -> from
            else -> null
        }?.let { from ->
            from.filterIsInstance<String>().takeIf(List<*>::isNotEmpty)?.toTypedArray()?.let { from ->
                receiver.from(*from)
            }

            from.filterIsInstance<From>().forEach { (mergePath, mergeSpec) ->
                receiver.from(mergePath) {
                    mergeSpec.applyTo(this)
                }
            }
        }
    }
}
