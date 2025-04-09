package gradle.plugins.initialization

import gradle.api.initialization.ConfigurableIncludedBuild
import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = IncludeBuildMapTransformingSerializer::class)
internal data class IncludeBuild(
    val rootProject: String,
    val configuration: ConfigurableIncludedBuild? = null,
)

private object IncludeBuildMapTransformingSerializer
    : MapTransformingSerializer<IncludeBuild>(
    IncludeBuild.serializer(),
    "rootProject",
)
