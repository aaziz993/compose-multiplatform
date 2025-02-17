package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TemplateProperties(
    override val aliases: List<Alias>,
    override val settings: ModuleSettings? = null
) : Properties
