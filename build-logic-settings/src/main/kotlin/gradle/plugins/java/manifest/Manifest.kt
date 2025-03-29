package gradle.plugins.java.manifest

import gradle.act
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class Manifest(
    val attributes: SerializableAnyMap? = null,
    val setAttributes: SerializableAnyMap? = null,
    val sections: Map<String, SerializableAnyMap>? = null,
    val setSections: Map<String, SerializableAnyMap>? = null,
    val effectiveManifest: Manifest? = null,
    val from: @Serializable(with = FromTransformingSerializer::class) Set<Any>? = null,
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

        from?.let { from ->
            from.filterIsInstance<String>().toTypedArray().let(receiver::from)

            from.filterIsInstance<From>().forEach { (mergePath, mergeSpec) ->
                receiver.from(mergePath) {
                    mergeSpec.applyTo(this)
                }
            }
        }
    }
}
