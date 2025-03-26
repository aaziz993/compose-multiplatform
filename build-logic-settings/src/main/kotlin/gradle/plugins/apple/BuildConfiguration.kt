package gradle.plugins.apple

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import gradle.plugins.android.BuildType
import kotlinx.serialization.KSerializer
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
        properties?.let(receiver.properties::putAll)
        setProperties?.act(receiver.properties::clear)?.let(receiver.properties::putAll)
    }
}

internal object BuildConfigurationKeyTransformingSerializer
    : NamedKeyTransformingSerializer<BuildConfiguration>(BuildConfiguration.serializer())
