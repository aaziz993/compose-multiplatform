package gradle.api.ci

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PublishRepositoryMapTransformingSerializer::class)
internal data class PublishRepository(
    val name: String,
    val enabled: Boolean = true
)

private object PublishRepositoryMapTransformingSerializer :
    MapTransformingSerializer<PublishRepository>(
        PublishRepository.serializer(),
        "name",
        "enabled",
    )
