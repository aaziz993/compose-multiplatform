package gradle.api.artifacts

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = SubstituteMapTransformingSerializer::class)
internal data class Substitute(
    val componentSelector: String,
    val substitution: DependencySubstitutions.Substitution? = null,
)

private object SubstituteMapTransformingSerializer :
    MapTransformingSerializer<Substitute>(
        Substitute.generatedSerializer(),
        "componentSelector",
    )
