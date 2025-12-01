package clib.data.config

import clib.presentation.theme.model.Theme
import kotlinx.serialization.Serializable

@Serializable
public data class ComposeConfig(
    val theme: Theme? = null,
    val locales: List<String>? = null,
    val locale: String? = null,
)
