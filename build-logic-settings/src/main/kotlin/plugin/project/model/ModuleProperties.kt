package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ModuleProperties(
    val group: String? = null,
    val description: String? = null,
    val version: Version = Version(),
    val apply: List<String> = emptyList(),
    @Transient
    var templates: Map<String, TemplateProperties> = emptyMap(),
    override val settings: ModuleSettings = ModuleSettings(),
) : Properties
