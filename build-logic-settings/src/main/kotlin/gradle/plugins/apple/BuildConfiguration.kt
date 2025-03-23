package gradle.plugins.apple

import gradle.api.ProjectNamed
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.gradle.apple.BuildConfiguration

@Serializable
internal data class BuildConfiguration(
    override val name: String? = null,
    val fatFrameworks: Boolean? = null,
    val properties: SerializableAnyMap? = null,
    val setProperties: SerializableAnyMap? = null
) : ProjectNamed<BuildConfiguration> {

    context(Project)
    override fun applyTo(recipient: BuildConfiguration) {
        recipient::fatFrameworks trySet fatFrameworks
        properties?.let(recipient.properties::putAll)
        setProperties?.act(recipient.properties::clear)?.let(recipient.properties::putAll)
    }
}
