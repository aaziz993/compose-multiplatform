package gradle.api.artifacts

import gradle.api.catalog.NotationContentPolymorphicSerializer
import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = TargetObjectTransformingSerializer::class)
internal data class Target(
    val notation: @Serializable(with = NotationContentPolymorphicSerializer::class) Any,
    val reason: String? = null
)

private object TargetObjectTransformingSerializer :
    JsonObjectTransformingSerializer<Target>(
        Target.generatedSerializer(),
        "notation",
        "reason",
    )
