package gradle.plugins.initialization

import gradle.api.initialization.ConfigurableIncludedBuild
import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = IncludeBuildObjectTransformingSerializer::class)
internal data class IncludeBuild(
    val rootProject: String,
    val configuration: ConfigurableIncludedBuild? = null,
)

private object IncludeBuildObjectTransformingSerializer
    : JsonObjectTransformingSerializer<IncludeBuild>(
    IncludeBuild.serializer(),
    "rootProject",
)
