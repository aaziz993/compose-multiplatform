package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ModuleProperties(
    val apply: Map<String, TemplateProperties> = emptyMap(),
    override val aliases: List<Alias> = emptyList(),
    override val settings: Settings = Settings(),
) : Properties
