package plugin.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import plugin.project.model.target.Target

@Serializable
internal data class ModuleProperties(
    val application: Boolean = false,
    val targets: List<Target> = emptyList(),
    val aliases: List<Alias> = emptyList(),
    val group: String? = null,
    val description: String? = null,
    val apply: List<String> = emptyList(),
    val dependencies: List<Dependency> = emptyList(),
    @SerialName("test-dependencies")
    val testDependencies: List<Dependency> = emptyList(),
    val settings: ModuleSettings = ModuleSettings(),
    @SerialName("test-settings")
    val testSettings: ModuleSettings? = null
)
