package klib.data.config.locale

import klib.data.location.locale.Locale
import klib.data.location.locale.current
import kotlinx.serialization.Serializable

@Serializable
public data class Localization(
    val locales: List<Locale> = emptyList(),
    val locale: Locale = Locale.current,
    val weblates: WeblateConfig? = null,
)
