package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TemplateProperties(
    val aliases: List<Alias> = emptyList(),
    override val settings: ModuleSettings? = null
) : Properties
