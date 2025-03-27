package gradle.plugins.apple

import gradle.act
import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.tryPutAll
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
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

internal object BuildConfigurationKeyTransformingSerializer
    : NamedKeyTransformingSerializer<BuildConfiguration>(BuildConfiguration.serializer())
