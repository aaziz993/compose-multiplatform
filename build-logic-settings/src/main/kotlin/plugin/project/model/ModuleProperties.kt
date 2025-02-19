package plugin.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ModuleProperties(
    val aliases: List<Alias> = emptyList(),
    val group: String? = null,
    val description: String? = null,
    val apply: List<String> = emptyList(),
    val dependencies: List<Dependency>? = null,
    @SerialName("test-dependencies")
    val testDependencies: List<Dependency>? = null,
    val settings: ModuleSettings = ModuleSettings(),
    @SerialName("test-settings")
    val testSettings: ModuleSettings? = null
)
