package klib.data.location.locale.weblate.model

import kotlinx.serialization.Serializable

@Serializable
public data class WeblateComponent(
    val name: String,
    val slug: String,
    val project: WeblateProject,
)
