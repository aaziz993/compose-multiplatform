package gradle.api.artifacts

import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = DependencyArtifactSelectorObjectTransformingSerializer::class)
internal data class DependencyArtifactSelector(
    val type: String,
    val extension: String? = null,
    val classifier: String? = null,
) : org.gradle.api.artifacts.DependencyArtifactSelector {

    override fun getType(): String = type

    override fun getExtension(): String? = extension

    override fun getClassifier(): String? = classifier
}

private object DependencyArtifactSelectorObjectTransformingSerializer :
    JsonObjectTransformingSerializer<DependencyArtifactSelector>(
        DependencyArtifactSelector.generatedSerializer(),
        "type",
    )
