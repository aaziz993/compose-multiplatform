package gradle.plugins.apple

import gradle.serialization.serializer.AnySerializer
import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.gradle.apple.BuildConfiguration

@Serializable
internal data class BuildConfiguration(
    val fatFrameworks: Boolean? = null,
    val name: String = "",
    val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null
) {

    fun applyTo(configuration: BuildConfiguration) {
        configuration::fatFrameworks trySet fatFrameworks
        properties?.let(configuration.properties::putAll)
    }
}
