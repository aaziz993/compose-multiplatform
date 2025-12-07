package klib.data.location.locale.weblate.model

import kotlinx.serialization.Serializable

@Serializable
public data class WeblateTranslation(
    val language: WeblateLanguage,
    val component: WeblateComponent,
)
