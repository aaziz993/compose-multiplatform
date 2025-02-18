package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ModuleProperties(
    val apply: List<String> = emptyList(),
    @Transient
    var templates: Map<String, TemplateProperties> = emptyMap(),
    override val settings: ModuleSettings = ModuleSettings(),
) : Properties
