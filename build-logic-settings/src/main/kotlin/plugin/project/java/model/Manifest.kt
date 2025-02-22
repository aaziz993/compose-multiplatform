package plugin.project.java.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class Manifest(
    val attributes: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
)
