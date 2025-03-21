package gradle.plugins.buildconfig

import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class ForClass(
    val packageName: String? = null,
    val className: String,
    val configureAction: BuildConfigClassSpecImpl? = null,
)

internal object ForClassTransformingSerializer : KeyTransformingSerializer<ForClass>(
    ForClass.serializer(),
    "className"
)
