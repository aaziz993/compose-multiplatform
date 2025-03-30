package gradle.api.ci

import gradle.api.EnabledSettings
import gradle.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PublishRepositoryObjectTransformingSerializer::class)
internal data class PublishRepository(
    val name: String,
    override val enabled: Boolean = true
) : EnabledSettings

private object PublishRepositoryObjectTransformingSerializer :
    JsonObjectTransformingSerializer<PublishRepository>(
        PublishRepository.serializer(),
        "name",
        "enabled",
    )
