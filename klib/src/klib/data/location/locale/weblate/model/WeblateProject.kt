package klib.data.location.locale.weblate.model

import kotlinx.serialization.Serializable

@Serializable
public data class WeblateProject(
    val name: String,
    val slug: String,
)
