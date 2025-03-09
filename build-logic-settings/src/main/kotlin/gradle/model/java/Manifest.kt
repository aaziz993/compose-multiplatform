package gradle.model.java

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.java.archives.Manifest

@Serializable
internal data class Manifest(
    val attributes: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
) {

    context(Project)
    fun applyTo(manifest: Manifest) {
        manifest.attributes.putAll(
            mapOf(
                "Implementation-Title" to (description ?: name),
                "Implementation-Version" to version,
                "Automatic-Module-Name" to name,
            ),
        )

        attributes?.let(manifest.attributes::putAll)
    }
}
