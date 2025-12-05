package clib.presentation.components

import kotlinx.serialization.Serializable

@Serializable
public data class Components(
    val quickAccess: QuickAccess = QuickAccess(),
    val connectivity: Connectivity = Connectivity()
)
