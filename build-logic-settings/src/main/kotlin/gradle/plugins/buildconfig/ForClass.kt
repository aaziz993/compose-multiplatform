package gradle.plugins.buildconfig

import gradle.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ForClass(
    val packageName: String? = null,
    val className: String,
    val configureAction: BuildConfigClassSpecImpl? = null,
)

internal object ForClassObjectTransformingSerializer : JsonObjectTransformingSerializer<ForClass>(
    ForClass.serializer(),
    "className",
)
