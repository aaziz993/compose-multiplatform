package plugin.project.java.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.java.archives.Manifest

@Serializable
internal data class Manifest(
    val attributes: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
) {

    fun applyTo(manifest: Manifest) {
        attributes?.let(manifest.attributes::putAll)
    }
}
