package gradle.api.ci

import klib.data.type.serialization.serializers.transform.MapTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PublishRepositoryMapTransformingSerializer::class)
public data class PublishRepository(
    val name: String,
    val enabled: Boolean = true
)

private object PublishRepositoryMapTransformingSerializer :
    MapTransformingSerializer<PublishRepository>(
        PublishRepository.serializer(),
        "name",
        "enabled",
    )
