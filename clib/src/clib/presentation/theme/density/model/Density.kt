package clib.presentation.theme.density.model

import androidx.compose.ui.unit.Density
import kotlinx.serialization.Serializable

@Serializable
public data class Density(
    val density: Float,
    val fontScale: Float,
) {

    public fun toDensity(): androidx.compose.ui.unit.Density = Density(density, fontScale)
}

public fun Density.toDensity(): Density = Density(density, fontScale)
