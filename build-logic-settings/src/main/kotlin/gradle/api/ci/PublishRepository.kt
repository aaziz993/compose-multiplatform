package gradle.api.ci

import klib.data.type.serialization.json.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PublishRepositoryObjectTransformingSerializer::class)
internal data class PublishRepository(
    val name: String,
    val enabled: Boolean = true
)

private object PublishRepositoryObjectTransformingSerializer :
    JsonObjectTransformingSerializer<PublishRepository>(
        PublishRepository.serializer(),
        "name",
        "enabled",
    )
