package presentation.components.textfield.search.model

import kotlinx.serialization.Serializable

@Serializable
public data class SearchFieldStateData(
    val value: String = "",
    val matchAll: Boolean = true,
    val regexMatch: Boolean = false,
    val ignoreCase: Boolean = true,
    val compareMatch: SearchFieldCompare = SearchFieldCompare.EQUALS
)
