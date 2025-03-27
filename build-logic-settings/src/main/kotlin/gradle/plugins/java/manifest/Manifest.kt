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
    val froms: LinkedHashSet<@Serializable(with = FromContentPolymorphicSerializer::class) Any>? = null,
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

        receiver::attributes trySet attributes
        setAttributes?.act(receiver.attributes::clear)?.let(receiver.attributes::putAll)

        sections?.forEach { (key, value) ->
            receiver.attributes(value, key)
        }

        setSections?.act(receiver.sections::clear)?.forEach { (key, value) ->
            receiver.attributes(value, key)
        }

        effectiveManifest?.applyTo(receiver.effectiveManifest)

receiver::from trySet         froms?.filterIsInstance<String>()

        froms?.filterIsInstance<From>()?.toTypedArray()?.forEach { (mergePath, mergeSpec) ->
            receiver.from(mergePath, mergeSpec::applyTo)
        }
    }
}
