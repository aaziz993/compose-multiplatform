package gradle.plugins.buildconfig

import gradle.serialization.serializer.JsonKeyValueTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ForClass(
    val packageName: String? = null,
    val className: String,
    val configureAction: BuildConfigClassSpecImpl? = null,
)

internal object ForClassKeyValueTransformingSerializer : JsonKeyValueTransformingSerializer<ForClass>(
    ForClass.serializer(),
    "className",
)
