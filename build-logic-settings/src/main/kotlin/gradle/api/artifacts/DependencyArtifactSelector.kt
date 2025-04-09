package gradle.api.artifacts

import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = DependencyArtifactSelectorMapTransformingSerializer::class)
internal data class DependencyArtifactSelector(
    val type: String,
    val extension: String? = null,
    val classifier: String? = null,
) : org.gradle.api.artifacts.DependencyArtifactSelector {

    override fun getType(): String = type

    override fun getExtension(): String? = extension

    override fun getClassifier(): String? = classifier
}

private object DependencyArtifactSelectorMapTransformingSerializer :
    MapTransformingSerializer<DependencyArtifactSelector>(
        DependencyArtifactSelector.generatedSerializer(),
        "type",
    )
