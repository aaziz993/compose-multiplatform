package gradle.plugins.apple

import gradle.api.NamedMapTransformingSerializer
import gradle.api.ProjectNamed
import klib.data.type.serialization.serializer.SerializableAnyMap
import klib.data.type.collection.tryPutAll
import klib.data.type.collection.trySet
import klib.data.type.reflection.trySet
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = BuildConfigurationMapTransformingSerializer::class)
internal data class BuildConfiguration(
    override val name: String? = null,
    val fatFrameworks: Boolean? = null,
    val properties: SerializableAnyMap? = null,
    val setProperties: SerializableAnyMap? = null
) : ProjectNamed<org.jetbrains.gradle.apple.BuildConfiguration> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.gradle.apple.BuildConfiguration) {
        receiver::fatFrameworks trySet fatFrameworks
        receiver.properties tryPutAll properties
        receiver.properties trySet setProperties
    }
}

private object BuildConfigurationMapTransformingSerializer
    : NamedMapTransformingSerializer<BuildConfiguration>(BuildConfiguration.generatedSerializer())
