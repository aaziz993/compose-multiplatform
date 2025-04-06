package gradle.api.artifacts

import klib.data.type.serialization.json.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = SubstituteObjectTransformingSerializer::class)
internal data class Substitute(
    val componentSelector: String,
    val substitution: DependencySubstitutions.Substitution? = null,
)

private object SubstituteObjectTransformingSerializer :
    JsonObjectTransformingSerializer<Substitute>(
        Substitute.generatedSerializer(),
        "componentSelector",
    )
