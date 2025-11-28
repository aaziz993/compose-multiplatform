package clib.presentation.theme.density.model

import kotlinx.serialization.Serializable

@Serializable
public data class Density(
    val density: Float = 2f,
    val fontScale: Float = 1f,
) {

    public fun toDensity(): androidx.compose.ui.unit.Density = androidx.compose.ui.unit.Density(density, fontScale)
}

public fun androidx.compose.ui.unit.Density.toDensity(): Density = Density(density, fontScale)
