package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TemplateProperties(
    val aliases: List<Alias> = emptyList(),
    val dependencies: List<Dependency>? = null,
    override val settings: ModuleSettings? = null
) : Properties
