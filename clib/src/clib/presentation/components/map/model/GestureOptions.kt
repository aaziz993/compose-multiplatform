package clib.presentation.components.map.model

import kotlinx.serialization.Serializable

@Serializable
public data class GestureOptions(
    val isZoomEnabled: Boolean = true,
    val isRotateEnabled: Boolean = true,
    val isMoveEnabled: Boolean = true,
)
