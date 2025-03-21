package gradle.plugins.java

import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.java.archives.Manifest

@Serializable
internal data class Manifest(
    val attributes: SerializableAnyMap? = null,
    val setAttributes: SerializableAnyMap? = null,
    val sections: Map<String, SerializableAnyMap>? = null,
    val setSections: Map<String, SerializableAnyMap>? = null,
    val effectiveManifest: gradle.plugins.java.Manifest? = null
) {

    context(Project)
    fun applyTo(recipient: Manifest) {
        recipient.attributes.putAll(
            mapOf(
                "Implementation-Title" to (description ?: name),
                "Implementation-Version" to version,
                "Automatic-Module-Name" to name,
            ),
        )

        attributes?.let(recipient::attributes)
        setAttributes?.act(recipient.attributes::clear)?.let(recipient.attributes::putAll)

        sections?.forEach { (key, value) ->
            recipient.attributes(value, key)
        }

        setSections?.forEach { (key, value) ->
            recipient.sections.clear()
            recipient.attributes(value, key)
        }

        effectiveManifest?.applyTo(recipient.effectiveManifest)
    }
}
