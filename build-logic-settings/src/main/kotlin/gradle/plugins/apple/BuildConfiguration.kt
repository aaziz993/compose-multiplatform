package gradle.plugins.apple

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.BuildConfiguration

@Serializable
internal data class BuildConfiguration(
    val fatFrameworks: Boolean? = null,
    val name: String = "",
    val properties: SerializableAnyMap? = null,
    val setProperties: SerializableAnyMap? = null
) {

    fun applyTo(recipient: BuildConfiguration) {
        recipient::fatFrameworks trySet fatFrameworks
        properties?.let(recipient.properties::putAll)
        setProperties?.act(recipient.properties::clear)?.let(recipient.properties::putAll)
    }
}
