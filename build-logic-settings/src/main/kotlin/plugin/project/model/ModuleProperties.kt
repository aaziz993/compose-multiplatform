package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.web.model.WebProduct

@Serializable
internal data class ModuleProperties(
    val product: WebProduct = WebProduct(),
    val apply: Map<String, TemplateProperties> = emptyMap(),
    override val aliases: List<Alias> = emptyList(),
    override val settings: Settings = Settings(),
) : Properties
