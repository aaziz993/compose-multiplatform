package plugin.project.model.module

import kotlinx.serialization.Serializable

@Serializable
internal data class ModuleProperties(
    val apply: Map<String, TemplateProperties> = emptyMap(),
    override val aliases: List<Alias> = emptyList(),
    override val settings: ModuleSettings = ModuleSettings(),
) : Properties
