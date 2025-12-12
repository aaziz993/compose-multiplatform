package klib.data.location.locale.weblate.model

import kotlinx.serialization.Serializable

@Serializable
public data class WeblateUnit(
    val translation: String,
    val context: String,
    val note: String,
    val source: List<String>,
    val target: List<String>
)
