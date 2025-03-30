package gradle.api.ci


import gradle.serialization.serializer.JsonObjectTransformingSerializer
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
