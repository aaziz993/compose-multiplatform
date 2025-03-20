package gradle.plugins.apple

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.BuildConfiguration

@Serializable
internal data class BuildConfiguration(
    val fatFrameworks: Boolean? = null,
    val name: String = "",
    val properties: SerializableAnyMap? = null
) {

    fun applyTo(recipient: BuildConfiguration) {
        configuration::fatFrameworks trySet fatFrameworks
        properties?.let(configuration.properties::putAll)
    }
}
