package gradle.plugins.java.manifest

import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
internal data class Manifest(
    val attributes: SerializableAnyMap? = null,
    val setAttributes: SerializableAnyMap? = null,
    val sections: Map<String, SerializableAnyMap>? = null,
    val setSections: Map<String, SerializableAnyMap>? = null,
    val effectiveManifest: Manifest? = null,
    val from: @Serializable(with = FromSerializer::class) Any? = null,
) {

    context(Project)
    fun applyTo(recipient: org.gradle.api.java.archives.Manifest) {
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

        when (val from = from) {
            is String -> recipient.from(from)
            is LinkedHashSet<*> -> recipient.from(*from.toTypedArray())
            is From -> recipient.from(from.mergePath, from.mergeSpec::applyTo)

            else -> Unit
        }
    }
}

internal object FromSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> String.serializer()
            is JsonArray -> SetSerializer(String.serializer())
            is JsonObject -> From.serializer()
        }
}
