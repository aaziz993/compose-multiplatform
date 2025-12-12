package clib.presentation.components

import kotlinx.serialization.Serializable

@Serializable
public data class Components(
    val appBar: AppBar = AppBar(),
    val connectivity: Connectivity = Connectivity(),
)
