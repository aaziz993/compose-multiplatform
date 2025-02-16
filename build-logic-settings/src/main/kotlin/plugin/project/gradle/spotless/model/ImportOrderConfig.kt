package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ImportOrderConfig(
    val importOrder: List<String>? = null,
    val importOrderFile: String? = null,
    val wildcardsLast: Boolean?=null,
    val semanticSort: Boolean?=null,
    val treatAsPackage: Set<String>? = null,
    val treatAsClass: Set<String>? = null
)
