package gradle.plugins.buildconfig

import gradle.serialization.serializer.KeyValueTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ForClass(
    val packageName: String? = null,
    val className: String,
    val configureAction: BuildConfigClassSpecImpl? = null,
)

internal object ForClassKeyValueTransformingSerializer : KeyValueTransformingSerializer<ForClass>(
    ForClass.serializer(),
    "className",
)
